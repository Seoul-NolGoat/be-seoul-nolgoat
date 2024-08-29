package wad.seoul_nolgoat.web.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wad.seoul_nolgoat.service.auth.AuthService;
import wad.seoul_nolgoat.web.auth.dto.response.UserProfileDto;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class AuthController {

    private final AuthService authService;

    @GetMapping("/user_profile")
    public ResponseEntity<UserProfileDto> showUserProfile(HttpServletRequest request) {
        String authorization = request.getHeader(AuthService.AUTHORIZATION_HEADER_TYPE);
        if (authorization == null || authorization.isBlank()) {
            return ResponseEntity
                    .noContent()
                    .build();
        }

        return ResponseEntity
                .ok(authService.findLoginUserByAuthorization(authorization));
    }

    @PostMapping("/reissue")
    public ResponseEntity reissueTokens(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = request.getHeader(AuthService.REFRESH_HEADER_TYPE);
        if (authService.isInvalidRefreshToken(refreshToken)
                || authService.isExpiredToken(refreshToken)
                || !authService.isExistRefreshToken(refreshToken)) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .build();
        }
        authService.deleteRefreshToken(refreshToken);
        String newRefreshToken = authService.getNewRefreshToken(refreshToken);
        authService.saveRefreshToken(newRefreshToken);

        response.setHeader(AuthService.AUTHORIZATION_HEADER_TYPE, authService.getNewAccessToken(refreshToken));
        response.setHeader(AuthService.REFRESH_HEADER_TYPE, newRefreshToken);

        return ResponseEntity
                .ok()
                .build();
    }
}
