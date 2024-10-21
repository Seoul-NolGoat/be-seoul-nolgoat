package wad.seoul_nolgoat.web.auth;

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
@RestController
@RequestMapping("/api/auths/")
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
        String refreshToken = request.getHeader("Refresh");
        /*if (!jwtService.isValidRefreshToken(refreshToken) || !jwtService.isExistRefreshToken(refreshToken)) {
            throw new ApiException(ErrorCode.)
        }*/
        jwtService.deleteRefreshToken(refreshToken);
        String newRefreshToken = jwtService.reissueRefreshToken(refreshToken);
        jwtService.saveRefreshToken(newRefreshToken);

        response.setHeader("Authorization", jwtService.reissueAccessToken(refreshToken));
        response.setHeader("Refresh", newRefreshToken);

        return ResponseEntity
                .ok()
                .build();
    }
}
