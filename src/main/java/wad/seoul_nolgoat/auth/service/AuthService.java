package wad.seoul_nolgoat.auth.service;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import wad.seoul_nolgoat.auth.jwt.JwtProvider;
import wad.seoul_nolgoat.exception.ApiException;

import static wad.seoul_nolgoat.auth.jwt.JwtProvider.*;
import static wad.seoul_nolgoat.auth.service.TokenService.REFRESH_TOKEN_PREFIX;
import static wad.seoul_nolgoat.exception.ErrorCode.*;

@RequiredArgsConstructor
@Service
public class AuthService {

    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtProvider jwtProvider;
    private final TokenService tokenService;

    // Refresh 토큰 생성 및 저장
    public String createRefreshToken(String loginId) {
        String refreshToken = jwtProvider.createToken(
                loginId,
                CLAIM_TYPE_REFRESH,
                REFRESH_TOKEN_EXPIRATION_TIME
        );

        String key = REFRESH_TOKEN_PREFIX + loginId;
        tokenService.saveToken(
                key,
                refreshToken,
                jwtProvider.getExpiration(refreshToken)
        );

        return refreshToken;
    }

    // Access 토큰 생성
    public String createAccessToken(String loginId) {
        return jwtProvider.createToken(
                loginId,
                CLAIM_TYPE_ACCESS,
                ACCESS_TOKEN_EXPIRATION_TIME
        );
    }

    // Refresh 토큰을 이용해 Access 토큰 재발급
    public String reissueAccessToken(String refreshToken) {
        return jwtProvider.createToken(
                getLoginId(refreshToken),
                CLAIM_TYPE_ACCESS,
                ACCESS_TOKEN_EXPIRATION_TIME
        );
    }

    // Test 토큰 생성
    public String createTestToken(String loginId, String type, Long expirationTime) {
        return jwtProvider.createToken(
                loginId,
                type,
                expirationTime
        );
    }

    // Refresh 토큰 검증
    public void verifyRefreshToken(String refreshToken, HttpServletResponse response) {
        if (refreshToken == null) {
            throw new ApiException(NULL_REFRESH_TOKEN);
        }
        try {
            String key = REFRESH_TOKEN_PREFIX + getLoginId(refreshToken);

            // 캐시에 Refresh 토큰이 존재하는지 확인
            if (tokenService.getToken(key) == null) {
                throw new ApiException(REFRESH_TOKEN_NOT_FOUND);
            }
            if (!jwtProvider.getType(refreshToken).equals(CLAIM_TYPE_REFRESH)) {
                throw new ApiException(INVALID_TOKEN_TYPE);
            }
        } catch (ExpiredJwtException e) {
            deleteRefreshTokenCookie(response);
            throw new ApiException(TOKEN_EXPIRED);
        } catch (JwtException e) { // ExpiredJwtException을 제외한 나머지 JwtException 처리
            throw new ApiException(INVALID_TOKEN_FORMAT);
        }
    }

    // 인증 헤더 검증
    public void verifyAuthorization(String authorization) {
        if (authorization == null || !authorization.startsWith(BEARER_PREFIX)) {
            throw new ApiException(INVALID_AUTHORIZATION_HEADER);
        }
    }

    // Access 토큰 검증
    public void verifyAccessToken(String accessToken) {
        if (!jwtProvider.getType(accessToken).equals(CLAIM_TYPE_ACCESS)) {
            throw new ApiException(INVALID_TOKEN_TYPE);
        }
    }

    public void deleteRefreshToken(String loginId) {
        tokenService.deleteToken(REFRESH_TOKEN_PREFIX + loginId);
    }

    // Refresh 토큰 만료 시, 쿠키 삭제
    public void deleteRefreshTokenCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie(REFRESH_TOKEN_COOKIE_NAME, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public String getLoginId(String token) {
        return jwtProvider.getLoginId(token);
    }
}
