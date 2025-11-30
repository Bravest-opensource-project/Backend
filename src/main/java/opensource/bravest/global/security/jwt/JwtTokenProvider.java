package opensource.bravest.global.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.access-token-validity-seconds}")
    private long accessValidity;

    @Value("${jwt.refresh-token-validity-seconds}")
    private long refreshValidity;

    private SecretKey key;

    @PostConstruct
    void init() {
        if (secret == null || secret.isBlank()) {
            throw new IllegalStateException("jwt.secret is not configured. Check your application.yml / env.");
        }

        byte[] keyBytes;
        try {
            // secret이 Base64면 여기서 정상 디코딩
            keyBytes = Decoders.BASE64.decode(secret);
        } catch (IllegalArgumentException e) {
            // Base64 아니면 그냥 문자열 바이트로 사용
            keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        }

        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createAccessToken(String subject, Map<String, Object> claims) {
        Instant now = Instant.now();
        return Jwts.builder().subject(subject).claims(claims).issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(accessValidity))).signWith(key).compact();
    }

    public String createRefreshToken(String subject) {
        Instant now = Instant.now();
        return Jwts.builder().subject(subject).issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(refreshValidity))).signWith(key).compact();
    }

    public Long getIdFromToken(String token) {
        Claims claims = Jwts.parser().verifyWith(key) // init()에서 만든 key 재사용
                .build().parseSignedClaims(token).getPayload();

        return claims.get("id", Long.class);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Claims parseClaims(String token) {
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
    }
}
