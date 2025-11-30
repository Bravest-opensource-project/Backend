package opensource.bravest.global.handler;

import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import opensource.bravest.domain.profile.repository.AnonymousProfileRepository;
import org.springframework.data.redis.core.StringRedisTemplate;
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

@Slf4j
@Component
@RequiredArgsConstructor
public class StompHandler implements ChannelInterceptor {

  private final AnonymousProfileRepository anonymousProfileRepository;
  private final StringRedisTemplate redisTemplate;

  private static final String USER_SUB_KEY_PREFIX = "ws:subs:user:"; // + anonymousId
  private static final String METRIC_TOTAL_SUB = "ws:metrics:sub:total";
  private static final String METRIC_DUP_SUB = "ws:metrics:sub:duplicate";

  @Override
  public Message<?> preSend(Message<?> message, MessageChannel channel) {
    StompHeaderAccessor accessor =
        MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

    if (accessor == null) {
      return message;
    }

    StompCommand command = accessor.getCommand();

    // 1) CONNECT: anonymousId를 Principal로 설정
    if (StompCommand.CONNECT.equals(command)) {
      String anonymousId = accessor.getFirstNativeHeader("anonymousId");
      if (anonymousId == null || anonymousId.isBlank()) {
        log.warn("STOMP CONNECT: anonymousId missing");
        throw new IllegalArgumentException("anonymousId header is required");
      }

      anonymousProfileRepository
          .findById(Long.valueOf(anonymousId))
          .ifPresentOrElse(
              member -> {
                Authentication auth =
                    new UsernamePasswordAuthenticationToken(
                        anonymousId, null, List.of(new SimpleGrantedAuthority("ROLE_ANONYMOUS")));
                SecurityContextHolder.getContext().setAuthentication(auth);
                accessor.setUser(auth);
                log.info("STOMP CONNECT: anonymousId={} principal set", anonymousId);
              },
              () -> {
                log.warn("STOMP CONNECT: invalid anonymousId={}", anonymousId);
                throw new IllegalArgumentException("Invalid anonymousId");
              });
    }

    // 2) SUBSCRIBE: Redis를 사용해 anonymousId 기준 중복 구독 방지 + 메트릭 기록
    if (StompCommand.SUBSCRIBE.equals(command)) {
      Principal user = accessor.getUser();
      if (user == null) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
          accessor.setUser(auth);
          user = auth;
        }
      }

      String destination = accessor.getDestination();

      if (user != null && destination != null) {
        String anonymousId = user.getName();
        String key = USER_SUB_KEY_PREFIX + anonymousId;

        log.info(
            "[SUBSCRIBE] handling: anonymousId={}, destination={}, key={}",
            anonymousId,
            destination,
            key);

        try {
          Long total = redisTemplate.opsForValue().increment(METRIC_TOTAL_SUB);
          Long added = redisTemplate.opsForSet().add(key, destination);
          redisTemplate.expire(key, java.time.Duration.ofHours(1));

          log.info("[SUBSCRIBE] redis result: total={}, added={}", total, added);

          if (added != null && added == 0L) {
            Long dup = redisTemplate.opsForValue().increment(METRIC_DUP_SUB);
            log.warn(
                "[SUBSCRIBE] duplicate detected: anonymousId={}, dest={}, dupCount={}",
                anonymousId,
                destination,
                dup);
            return null;
          }

          log.info("[SUBSCRIBE] stored in Redis: key={}, member={}", key, destination);

        } catch (Exception e) {
          log.error("Redis error while handling SUBSCRIBE", e);
        }
      } else {
        log.warn(
            "[SUBSCRIBE] skipped: user or destination is null (user={}, dest={})",
            user,
            destination);
      }
    }

    // 3) SEND: Principal 비어 있으면 SecurityContext에서 복구
    if (StompCommand.SEND.equals(command)) {
      Principal user = accessor.getUser();
      if (user == null) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
          accessor.setUser(auth);
        }
      }
    }

    // 4) DISCONNECT: 유저별 구독 키를 정리할지 여부 (옵션)
    //    - 전체 방 전체 유저 수가 크지 않다면 TTL만으로도 충분.
    if (StompCommand.DISCONNECT.equals(command)) {
      Principal user = accessor.getUser();
      if (user == null) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
          user = auth;
        }
      }
      if (user != null) {
        String anonymousId = user.getName();
        String key = USER_SUB_KEY_PREFIX + anonymousId;
        try {
          // 완전히 정리하고 싶으면 delete
          redisTemplate.delete(key);
          log.info("DISCONNECT: cleared subscriptions for anonymousId={}", anonymousId);
        } catch (Exception e) {
          log.error("Redis error while handling DISCONNECT", e);
        }
      }
    }

    return message;
  }
}
