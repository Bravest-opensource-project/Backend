package opensource.bravest.global.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import opensource.bravest.domain.profile.repository.AnonymousProfileRepository;
import opensource.bravest.global.security.jwt.JwtTokenProvider;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class StompHandler implements ChannelInterceptor {
    private final JwtTokenProvider jwtProvider;
    private final AnonymousProfileRepository anonymousProfileRepository;

    private final Map<String, Set<String>> userDestinations = new ConcurrentHashMap<>();

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor == null) {
            return message;
        }

        StompCommand command = accessor.getCommand();
        if (StompCommand.CONNECT.equals(command)) {
            String anonymousId = accessor.getFirstNativeHeader("anonymousId");
            if (anonymousId == null || anonymousId.isBlank()) {
                log.warn("STOMP CONNECT: anonymousId 누락");
                throw new IllegalArgumentException("anonymousId header is required");
            }

            anonymousProfileRepository.findById(Long.valueOf(anonymousId))
                    .ifPresentOrElse(member -> {
                        Authentication auth = new UsernamePasswordAuthenticationToken(
                                anonymousId,
                                null,
                                java.util.List.of(new SimpleGrantedAuthority("ROLE_ANONYMOUS"))
                        );
                        SecurityContextHolder.getContext().setAuthentication(auth);
                        accessor.setUser(auth);  // → @MessageMapping의 Principal로 전달
                        log.info("STOMP CONNECT: anonymousId={} Principal 설정 완료", anonymousId);}, () -> {
                        log.warn("STOMP CONNECT: 존재하지 않는 anonymousId={}", anonymousId);
                        throw new IllegalArgumentException("Invalid anonymousId");
                    });
        }

        if (StompCommand.SUBSCRIBE.equals(command)) {
            String destination = accessor.getDestination();
            Principal user = accessor.getUser();

            // CONNECT에서 setUser를 해줬다면 여기서 null이면 안 되는 게 정상이나,
            // 혹시 모르니 한 번 더 SecurityContext에서 복구 시도
            if (user == null) {
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                if (auth != null) {
                    accessor.setUser(auth);
                    user = auth;
                }
            }

            if (user != null && destination != null) {
                String userKey = user.getName(); // == anonymousId

                Set<String> dests =
                        userDestinations.computeIfAbsent(
                                userKey,
                                k -> ConcurrentHashMap.newKeySet()
                        );

                if (!dests.add(destination)) {
                    log.warn("중복 SUBSCRIBE 감지: anonymousId={}, destination={}",
                            userKey, destination);
                    // 이 SUBSCRIBE 프레임 자체를 무시
                    return null;
                }
            }
        }

        // 3) SEND에도 Principal이 비어 있으면 SecurityContext에서 복구
        if (StompCommand.SEND.equals(command)) {
            Principal user = accessor.getUser();
            if (user == null) {
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                if (auth != null) {
                    accessor.setUser(auth);
                }
            }
        }

        // (선택) DISCONNECT 시 userDestinations에서 정리
        if (StompCommand.DISCONNECT.equals(command)) {
            Principal user = accessor.getUser();
            if (user == null) {
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                if (auth != null) {
                    user = auth;
                }
            }
            if (user != null) {
                String key = user.getName();
                userDestinations.remove(key);
                log.info("DISCONNECT: anonymousId={} 구독 정보 제거", key);
            }
        }

        if (StompCommand.SEND.equals(command) || StompCommand.SUBSCRIBE.equals(command)) {
            Principal user = accessor.getUser();
            if (user == null) {
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                if (auth != null) {
                    accessor.setUser(auth);
                }
            }
        }
        return message;
    }
}
