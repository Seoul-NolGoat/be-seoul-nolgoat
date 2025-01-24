package wad.seoul_nolgoat.web.admin;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wad.seoul_nolgoat.auth.service.AuthService;
import wad.seoul_nolgoat.auth.service.RedisTokenService;
import wad.seoul_nolgoat.web.admin.dto.response.TestTokenDto;

import java.util.Date;

@Hidden
@RequiredArgsConstructor
@RequestMapping("/api/admins")
@RestController
public class AdminController {

    private static final String TEST_ID = "kakao_3656161397";

    private final AuthService authService;
    private final RedisTokenService redisTokenService;

    @PostMapping("/expired-token")
    public ResponseEntity<TestTokenDto> getExpiredToken() {
        String accessToken = authService.createTestToken(TEST_ID, "access", 1000L);
        String refreshToken = authService.createTestToken(TEST_ID, "refresh", 1000L);

        return ResponseEntity
                .ok(new TestTokenDto(accessToken, refreshToken));
    }

    @PostMapping("/test-token")
    public ResponseEntity<TestTokenDto> getTestToken() {
        String accessToken = authService.createTestToken(TEST_ID, "access", 3 * 60 * 1000L);
        String refreshToken = authService.createTestToken(TEST_ID, "refresh", 3 * 60 * 1000L);
        redisTokenService.saveToken(RedisTokenService.REFRESH_TOKEN_KEY_PREFIX + TEST_ID, refreshToken, new Date(System.currentTimeMillis() + 3 * 60 * 1000L));

        return ResponseEntity
                .ok(new TestTokenDto(accessToken, refreshToken));
    }
}
