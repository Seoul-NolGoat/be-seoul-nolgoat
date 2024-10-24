package wad.seoul_nolgoat.web.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wad.seoul_nolgoat.auth.jwt.JwtService;
import wad.seoul_nolgoat.service.refreshtoken.RefreshTokenService;
import wad.seoul_nolgoat.service.user.UserService;
import wad.seoul_nolgoat.web.auth.dto.response.UserProfileDto;

@RequiredArgsConstructor
@RequestMapping("/api/auths/")
@RestController
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    @GetMapping("/me")
    public ResponseEntity<UserProfileDto> showUserProfile(@AuthenticationPrincipal OAuth2User loginUser) {
        String loginId = loginUser.getName();
        return ResponseEntity
                .ok(userService.getLoginUserDetails(loginId));
    }

    @PostMapping("/token/reissue")
    public ResponseEntity<Void> reissueTokens(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = getRefreshToken(request);
        jwtService.verifyRefreshToken(refreshToken, response);

        // Refresh 토큰 검증에 성공하면 Access 토큰을 재발급
        response.setHeader(JwtService.AUTHORIZATION_HEADER, jwtService.reissueAccessToken(refreshToken));

        return ResponseEntity
                .ok()
                .build();
    }

    @PostMapping("/logout")
    public ResponseEntity<String> successLogout(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = getRefreshToken(request);
        jwtService.verifyRefreshToken(refreshToken, response);

        // DB 및 쿠키에서 Refresh 토큰 삭제
        refreshTokenService.deleteRefreshToken(refreshToken);
        jwtService.deleteRefreshTokenCookie(response);

        return ResponseEntity
                .ok()
                .build();
    }

    private String getRefreshToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(JwtService.REFRESH_TOKEN_COOKIE_NAME)) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
