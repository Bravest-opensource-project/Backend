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

@Slf4j
@Component
@RequiredArgsConstructor
public class StompHandler implements ChannelInterceptor {
    private final JwtTokenProvider jwtProvider;
    private final AnonymousProfileRepository anonymousProfileRepository;

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
        } else if (StompCommand.SEND.equals(command) || StompCommand.SUBSCRIBE.equals(command)) {
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
