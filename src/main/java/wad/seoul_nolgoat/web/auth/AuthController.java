package wad.seoul_nolgoat.web.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wad.seoul_nolgoat.auth.jwt.JwtService;
import wad.seoul_nolgoat.web.auth.dto.response.UserProfileDto;

@RequiredArgsConstructor
@RequestMapping("/api/auths/")
@RestController
public class AuthController {

    private final JwtService jwtService;

    @GetMapping("/me")
    public ResponseEntity<UserProfileDto> showUserProfile(HttpServletRequest request) {
        String authorization = request.getHeader(JwtService.AUTHORIZATION_HEADER);
        return ResponseEntity
                .ok(jwtService.findLoginUserByAuthorization(authorization));
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
