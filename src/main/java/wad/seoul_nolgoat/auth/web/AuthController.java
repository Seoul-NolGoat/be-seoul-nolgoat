package wad.seoul_nolgoat.auth.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import wad.seoul_nolgoat.auth.service.AuthService;

import static wad.seoul_nolgoat.auth.service.AuthService.*;

@Tag(name = "인증")
@RequiredArgsConstructor
@RequestMapping("/api/auths")
@RestController
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "토큰 재발급 요청")
    @PostMapping("/token/reissue")
    public ResponseEntity<Void> reissueTokens(
            @RequestHeader(CSRF_PROTECTION_UUID_HEADER) String csrfProtectionUuid,
            @CookieValue(value = REFRESH_TOKEN_COOKIE_NAME) String refreshToken,
            HttpServletResponse response
    ) {
        authService.verifyRefreshToken(refreshToken, response);
        authService.verifyCsrfProtectionUuid(csrfProtectionUuid);

        // Refresh 토큰 및 CSRF Protection UUID 검증에 성공하면 Access 토큰을 재발급
        String accessToken = authService.reissueAccessToken(refreshToken);
        response.setHeader(AUTHORIZATION_HEADER, accessToken);

        return ResponseEntity
                .ok()
                .build();
    }

    @Operation(summary = "로그아웃")
    @PostMapping("/logout")
    public ResponseEntity<String> logout(
            @AuthenticationPrincipal OAuth2User loginUser,
            @RequestHeader(AUTHORIZATION_HEADER) String authorizationHeader,
            @RequestHeader(CSRF_PROTECTION_UUID_HEADER) String csrfProtectionUuid,
            @CookieValue(value = REFRESH_TOKEN_COOKIE_NAME) String refreshToken,
            HttpServletResponse response
    ) {
        authService.verifyRefreshToken(refreshToken, response);
        authService.verifyCsrfProtectionUuid(csrfProtectionUuid);

        String loginId = loginUser.getName();

        // 캐시 및 쿠키에서 Refresh 토큰 삭제
        authService.deleteRefreshToken(loginId);
        authService.deleteRefreshTokenCookie(response);

        // Access 토큰 블랙리스트 처리
        authService.saveAccessTokenToBlacklist(authorizationHeader.split(" ")[1]);

        return ResponseEntity
                .ok()
                .build();
    }

    @Operation(summary = "회원 탈퇴")
    @DeleteMapping("/user/delete")
    public ResponseEntity<Void> deleteUserByLoginId(
            @AuthenticationPrincipal OAuth2User loginUser,
            @RequestHeader(AUTHORIZATION_HEADER) String authorizationHeader,
            @RequestHeader(CSRF_PROTECTION_UUID_HEADER) String csrfProtectionUuid,
            @CookieValue(value = REFRESH_TOKEN_COOKIE_NAME) String refreshToken,
            HttpServletResponse response
    ) {
        authService.verifyRefreshToken(refreshToken, response);
        authService.verifyCsrfProtectionUuid(csrfProtectionUuid);

        String loginId = loginUser.getName();

        // 캐시 및 쿠키에서 Refresh 토큰 삭제
        authService.deleteRefreshToken(loginId);
        authService.deleteRefreshTokenCookie(response);

        // Access 토큰 블랙리스트 처리
        authService.saveAccessTokenToBlacklist(authorizationHeader.split(" ")[1]);

        // 소셜 계정 연동 unlink 및 유저 상태 변경
        authService.unlinkSocialAccount(loginUser.getName());

        return ResponseEntity
                .noContent()
                .build();
    }
}
