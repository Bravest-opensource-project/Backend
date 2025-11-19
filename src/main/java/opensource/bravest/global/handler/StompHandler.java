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
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (accessor == null) {
            return message;
        }

        StompCommand command = accessor.getCommand();
        if (StompCommand.CONNECT.equals(command)) {
            String token = accessor.getFirstNativeHeader("Authorization");

            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
            }

            // 토큰 검증 후 인증 객체 생성
            if (token != null && jwtProvider.validateToken(token)) {
                Long id = jwtProvider.getIdFromToken(token);

                anonymousProfileRepository.findById(id).ifPresent(member -> {
                    Authentication authentication = new UsernamePasswordAuthenticationToken(
                        id, null
                    );
                    // SecurityContext에도 저장
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    // STOMP 세션 사용자로도 설정 -> @MessageMapping Principal로 전달됨
                    accessor.setUser(authentication);
                    log.info("STOMP 연결 인증 성공 및 Principal 설정: {}", id);
                });
            } else {
                log.warn("STOMP CONNECT 토큰 검증 실패 또는 토큰 누락");
            }
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
