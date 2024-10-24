package wad.seoul_nolgoat.web.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wad.seoul_nolgoat.auth.jwt.JwtService;
import wad.seoul_nolgoat.service.refreshtoken.RefreshTokenService;
import wad.seoul_nolgoat.web.admin.dto.response.TestTokenDto;

@RequiredArgsConstructor
@RequestMapping("/api/admins")
@RestController
public class AdminController {

    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/expired-token")
    public ResponseEntity<TestTokenDto> getExpiredToken() {
        String accessToken = jwtService.createTestToken("kakao_3656161397", "access", 1000L);
        String refreshToken = jwtService.createTestToken("kakao_3656161397", "refresh", 1000L);
        refreshTokenService.saveRefreshToken(refreshToken, jwtService.getLoginId(refreshToken));

        return ResponseEntity
                .ok(new TestTokenDto(accessToken, refreshToken));
    }

    @PostMapping("/test-token")
    public ResponseEntity<TestTokenDto> getTestToken() {
        String accessToken = jwtService.createTestToken("kakao_3656161397", "access", 3 * 60 * 1000L);
        String refreshToken = jwtService.createTestToken("kakao_3656161397", "refresh", 3 * 60 * 1000L);
        refreshTokenService.saveRefreshToken(refreshToken, jwtService.getLoginId(refreshToken));

        return ResponseEntity
                .ok(new TestTokenDto(accessToken, refreshToken));
    }
}
