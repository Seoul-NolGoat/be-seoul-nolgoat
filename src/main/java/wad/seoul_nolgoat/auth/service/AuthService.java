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
import static wad.seoul_nolgoat.auth.service.TokenService.ACCESS_TOKEN_PREFIX;
import static wad.seoul_nolgoat.auth.service.TokenService.REFRESH_TOKEN_PREFIX;
import static wad.seoul_nolgoat.exception.ErrorCode.*;

@RequiredArgsConstructor
@Service
public class AuthService {

    private static final String BEARER_PREFIX = "Bearer ";
    private static final int REFRESH_TOKEN_COOKIE_EXPIRATION_TIME = 14 * 24 * 60 * 60; // 14 days

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
        try {
            String key = REFRESH_TOKEN_PREFIX + getLoginId(refreshToken);

            // 캐시에 Refresh 토큰이 존재하는지 확인
            if (tokenService.getToken(key) == null) {
                throw new ApiException(REFRESH_TOKEN_NOT_FOUND);
            }

            // 토큰 발급자가 올바른지 확인
            if (jwtProvider.isTokenNotIssuedByDomain(refreshToken)) {
                throw new ApiException(INVALID_TOKEN_ISSUER);
            }

            // 토큰 타입이 Refresh인지 확인
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
    public void verifyAuthorizationHeader(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith(BEARER_PREFIX)) {
            throw new ApiException(INVALID_AUTHORIZATION_HEADER);
        }
    }

    // Access 토큰 검증
    public void verifyAccessToken(String accessToken) {
        String key = ACCESS_TOKEN_PREFIX + getLoginId(accessToken);

        // Access 토큰 블랙리스트 여부 확인
        if (tokenService.getToken(key) != null) {
            throw new ApiException(ACCESS_TOKEN_BLACKLISTED);
        }

        // 토큰 발급자가 올바른지 확인
        if (jwtProvider.isTokenNotIssuedByDomain(accessToken)) {
            throw new ApiException(INVALID_TOKEN_ISSUER);
        }

        // 토큰 타입이 Access인지 확인
        if (!jwtProvider.getType(accessToken).equals(CLAIM_TYPE_ACCESS)) {
            throw new ApiException(INVALID_TOKEN_TYPE);
        }
    }

    // 캐시에서 Refresh 토큰 삭제
    public void deleteRefreshToken(String loginId) {
        tokenService.deleteToken(REFRESH_TOKEN_PREFIX + loginId);
    }

    // 캐시에 Access 토큰 블랙리스트 처리
    public void saveAccessTokenToBlacklist(String accessToken) {
        String key = ACCESS_TOKEN_PREFIX + getLoginId(accessToken);
        tokenService.saveToken(
                key,
                accessToken,
                jwtProvider.getExpiration(accessToken)
        );
    }

    // Refresh 토큰을 쿠키에 저장
    public Cookie createRefreshTokenCookie(String refreshToken) {
        Cookie refreshTokenCookie = new Cookie(REFRESH_TOKEN_COOKIE_NAME, refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(REFRESH_TOKEN_COOKIE_EXPIRATION_TIME);
        return refreshTokenCookie;
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
