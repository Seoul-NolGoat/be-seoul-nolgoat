package wad.seoul_nolgoat.web.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
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
    public ResponseEntity<UserProfileDto> showUserProfile(@RequestHeader("Authorization") String authorization) {
        return ResponseEntity
                .ok(authService.findLoginUserByAuthorization(authorization));
    }
}
