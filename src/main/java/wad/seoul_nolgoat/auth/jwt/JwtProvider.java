package wad.seoul_nolgoat.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtProvider {

    public static final Long REFRESH_TOKEN_EXPIRATION_TIME = 14 * 24 * 60 * 60 * 1000L; // 14 days
    public static final Long ACCESS_TOKEN_EXPIRATION_TIME = 60 * 60 * 1000L; // 1 hour
    public static final String CLAIM_TYPE = "type";
    public static final String CLAIM_TYPE_REFRESH = "refresh";
    public static final String CLAIM_TYPE_ACCESS = "access";
    
    private final SecretKey secretKey;
    private final String domain;

    public JwtProvider(@Value("${spring.jwt.secret}") String secret, @Value("${spring.jwt.domain}") String domain) {
        this.secretKey = new SecretKeySpec(
                secret.getBytes(StandardCharsets.UTF_8),
                Jwts.SIG.HS256.key().build().getAlgorithm()
        );
        this.domain = domain;
    }

    public String createToken(
            String loginId,
            String type,
            Long expirationTime
    ) {
        return Jwts.builder()
                .claim(CLAIM_TYPE, type)
                .subject(loginId)
                .issuer(domain)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(secretKey)
                .compact();
    }

    public String getType(String token) {
        return getPayload(token).get(CLAIM_TYPE, String.class);
    }

    // ex) kakao_12345678
    public String getLoginId(String token) {
        return getPayload(token).getSubject();
    }

    public Date getExpiration(String token) {
        return getPayload(token).getExpiration();
    }

    public boolean isTokenNotIssuedByDomain(String token) {
        return !getPayload(token).getIssuer()
                .equals(domain);
    }

    private Claims getPayload(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
