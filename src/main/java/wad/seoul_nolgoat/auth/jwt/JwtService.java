package wad.seoul_nolgoat.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import wad.seoul_nolgoat.exception.ApiException;
import wad.seoul_nolgoat.service.refreshtoken.RefreshTokenService;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import static wad.seoul_nolgoat.exception.ErrorCode.*;

@Slf4j
@Service
public class JwtService {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String REFRESH_TOKEN_COOKIE_NAME = "refreshToken";
    public static final Long REFRESH_TOKEN_EXPIRATION_TIME = 7 * 24 * 60 * 60 * 1000L; // 7 days

    private static final Long ACCESS_TOKEN_EXPIRATION_TIME = 30 * 60 * 1000L; // 30 minutes
    private static final String CLAIM_TYPE_ACCESS = "access";
    private static final String CLAIM_TYPE_REFRESH = "refresh";
    private static final String CLAIM_TYPE = "type";
    private static final String BEARER_PREFIX = "Bearer ";

    private final SecretKey secretKey;
    private final String domain;
    private final RefreshTokenService refreshTokenService;

    public JwtService(
            @Value("${spring.jwt.secret}") String secret,
            @Value("${spring.jwt.domain}") String domain,
            RefreshTokenService refreshTokenService
    ) {
        this.secretKey = new SecretKeySpec(
                secret.getBytes(StandardCharsets.UTF_8),
                Jwts.SIG.HS256.key().build().getAlgorithm()
        );
        this.domain = domain;
        this.refreshTokenService = refreshTokenService;
    }

    // 테스트용 토큰 발급
    public String createTestToken(String loginId, String type, Long expirationTime) {
        return Jwts.builder()
                .claim(CLAIM_TYPE, type)
                .subject(loginId)
                .issuer(domain)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(secretKey)
                .compact();
    }

    // 토큰 생성 모음
    public String createAccessToken(String loginId) {
        return Jwts.builder()
                .claim(CLAIM_TYPE, CLAIM_TYPE_ACCESS)
                .subject(loginId)
                .issuer(domain)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION_TIME))
                .signWith(secretKey)
                .compact();
    }

    public String createRefreshToken(String loginId) {
        return Jwts.builder()
                .claim(CLAIM_TYPE, CLAIM_TYPE_REFRESH)
                .subject(loginId)
                .issuer(domain)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION_TIME))
                .signWith(secretKey)
                .compact();
    }

    public String reissueAccessToken(String refreshToken) {
        return Jwts.builder()
                .claim(CLAIM_TYPE, CLAIM_TYPE_ACCESS)
                .subject(getLoginId(refreshToken))
                .issuer(domain)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION_TIME))
                .signWith(secretKey)
                .compact();
    }

    public String reissueRefreshToken(String refreshToken) {
        return Jwts.builder()
                .claim(CLAIM_TYPE, CLAIM_TYPE_REFRESH)
                .subject(getLoginId(refreshToken))
                .issuer(domain)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION_TIME))
                .signWith(secretKey)
                .compact();
    }

    // 토큰 검증 모음
    public void verifyAuthorization(String authorization) {
        if (authorization == null || !authorization.startsWith(BEARER_PREFIX)) {
            throw new ApiException(INVALID_AUTHORIZATION_HEADER);
        }
    }

    public void verifyAccessToken(String accessToken) {
        Claims payload = getPayload(accessToken);
        String type = payload.get(CLAIM_TYPE, String.class);
        if (!type.equals(CLAIM_TYPE_ACCESS)) {
            throw new ApiException(INVALID_TOKEN_TYPE);
        }
    }

    public void verifyRefreshToken(String refreshToken, HttpServletResponse response) {
        if (refreshToken == null) {
            throw new ApiException(NULL_REFRESH_TOKEN);
        }
        refreshTokenService.verifyRefreshTokenExistence(refreshToken);

        try {
            Claims payload = getPayload(refreshToken);
            String type = payload.get(CLAIM_TYPE, String.class);
            if (!type.equals(CLAIM_TYPE_REFRESH)) {
                throw new ApiException(INVALID_TOKEN_TYPE);
            }
        } catch (ExpiredJwtException e) {
            refreshTokenService.deleteRefreshToken(refreshToken);
            deleteRefreshTokenCookie(response);
            throw new ApiException(TOKEN_EXPIRED);
        } catch (JwtException e) { // ExpiredJwtException을 제외한 나머지 JwtException 처리
            throw new ApiException(INVALID_TOKEN_FORMAT);
        }
    }

    private Claims getPayload(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // 클레임 조회 모음
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
                .getSubject();
    }

    // Refresh 토큰 만료 시, 쿠키 삭제
    public void deleteRefreshTokenCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie(REFRESH_TOKEN_COOKIE_NAME, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
