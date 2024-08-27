package wad.seoul_nolgoat.jwt;

import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    public static final Long ACCESS_JWT_EXPIRATION_TIME = 10 * 60 * 1000L; // 10 minutes
    public static final Long REFRESH_JWT_EXPIRATION_TIME = 24 * 60 * 60 * 1000L; // 24 hours
    public static final String CLAIM_TYPE_ACCESS = "access";
    public static final String CLAIM_TYPE_REFRESH = "refresh";

    private static final String CLAIM_TYPE = "type";
    private static final String CLAIM_LOGIN_ID = "loginId";

    private final SecretKey secretKey;

    public JwtUtil(@Value("${spring.jwt.secret}") String secret) {
        this.secretKey = new SecretKeySpec(
                secret.getBytes(StandardCharsets.UTF_8),
                Jwts.SIG.HS256.key().build().getAlgorithm()
        );
    }

    public String getType(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get(CLAIM_TYPE, String.class);
    }

    public String getLoginId(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get(CLAIM_LOGIN_ID, String.class);
    }

    public boolean isExpired(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration()
                .before(new Date());
    }

    public String createJwt(String type, String loginId, Long expiredMs) {
        return Jwts.builder()
                .claim(CLAIM_TYPE, type)
                .claim(CLAIM_LOGIN_ID, loginId)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey)
                .compact();
    }
}
