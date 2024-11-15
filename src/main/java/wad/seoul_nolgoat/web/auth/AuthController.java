package wad.seoul_nolgoat.web.auth;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import wad.seoul_nolgoat.auth.jwt.JwtProvider;
import wad.seoul_nolgoat.auth.service.AuthService;
import wad.seoul_nolgoat.service.user.UserService;
import wad.seoul_nolgoat.web.auth.dto.response.UserProfileDto;

@RequiredArgsConstructor
@RequestMapping("/api/auths")
@RestController
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
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
            @CookieValue(value = JwtProvider.REFRESH_TOKEN_COOKIE_NAME) String refreshToken,
            HttpServletResponse response
    ) {
        //String refreshToken = getRefreshToken(request);
        authService.verifyRefreshToken(refreshToken, response);

        // Refresh 토큰 검증에 성공하면 Access 토큰을 재발급
        response.setHeader(JwtProvider.AUTHORIZATION_HEADER, authService.reissueAccessToken(refreshToken));

        return ResponseEntity
                .ok()
                .build();
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(
            @AuthenticationPrincipal OAuth2User loginUser,
            @RequestHeader("Authorization") String authorization,
            @CookieValue(value = JwtProvider.REFRESH_TOKEN_COOKIE_NAME) String refreshToken,
            HttpServletResponse response
    ) {
        authService.verifyRefreshToken(refreshToken, response);

        String loginId = loginUser.getName();

        // 캐시 및 쿠키에서 Refresh 토큰 삭제
        authService.deleteRefreshToken(loginId);
        authService.deleteRefreshTokenCookie(response);

        // Access 토큰 블랙리스트 처리
        authService.saveAccessTokenToBlacklist(authorization.split(" ")[1]);

        return ResponseEntity
                .ok()
                .build();
    }
}
