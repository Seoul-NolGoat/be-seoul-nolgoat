package wad.seoul_nolgoat.web.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import wad.seoul_nolgoat.web.auth.dto.request.CustomUser;
import wad.seoul_nolgoat.web.auth.dto.response.UserProfileDto;

@RestController("/api")
public class AuthController {

    @GetMapping("/user_profile")
    public ResponseEntity<UserProfileDto> showUserProfile(@AuthenticationPrincipal CustomUser loginUser) {
        if (loginUser == null) {
            return ResponseEntity
                    .noContent()
                    .build();
        }
        return ResponseEntity
                .ok(new UserProfileDto(
                        loginUser.getLoginId(),
                        loginUser.getNickname(),
                        loginUser.getProfileImage()
                ));
    }
}
