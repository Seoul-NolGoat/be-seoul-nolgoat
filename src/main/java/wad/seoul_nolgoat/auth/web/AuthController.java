package wad.seoul_nolgoat.auth.web;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import wad.seoul_nolgoat.auth.service.AuthService;
import wad.seoul_nolgoat.auth.web.dto.response.UserProfileDto;
import wad.seoul_nolgoat.service.user.UserService;

import static wad.seoul_nolgoat.auth.jwt.JwtProvider.AUTHORIZATION_HEADER;
import static wad.seoul_nolgoat.auth.jwt.JwtProvider.REFRESH_TOKEN_COOKIE_NAME;

@RequiredArgsConstructor
@RequestMapping("/api/auths")
@RestController
public class AuthController {

    private final UserService userService;
    private final AuthService authService;

    @GetMapping("/me")
    public ResponseEntity<UserProfileDto> showUserProfile(@AuthenticationPrincipal OAuth2User loginUser) {
        String loginId = loginUser.getName();
        return ResponseEntity
                .ok(userService.getLoginUserDetails(loginId));
    }

    @PostMapping("/token/reissue")
    public ResponseEntity<Void> reissueTokens(
            @CookieValue(value = REFRESH_TOKEN_COOKIE_NAME) String refreshToken,
            HttpServletResponse response
    ) {
        authService.verifyRefreshToken(refreshToken, response);

        // Refresh 토큰 검증에 성공하면 Access 토큰을 재발급
        response.setHeader(AUTHORIZATION_HEADER, authService.reissueAccessToken(refreshToken));

        return ResponseEntity
                .ok()
                .build();
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(
            @AuthenticationPrincipal OAuth2User loginUser,
            @RequestHeader(AUTHORIZATION_HEADER) String authorizationHeader,
            @CookieValue(value = REFRESH_TOKEN_COOKIE_NAME) String refreshToken,
            HttpServletResponse response
    ) {
        authService.verifyRefreshToken(refreshToken, response);

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
}
