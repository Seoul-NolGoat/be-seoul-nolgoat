package wad.seoul_nolgoat.web.user;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import wad.seoul_nolgoat.auth.web.dto.response.UserProfileDto;
import wad.seoul_nolgoat.service.user.UserService;
import wad.seoul_nolgoat.web.user.dto.request.UserSaveDto;
import wad.seoul_nolgoat.web.user.dto.request.UserUpdateDto;
import wad.seoul_nolgoat.web.user.dto.response.UserDetailsDto;

import java.net.URI;

@Hidden
@RequiredArgsConstructor
@RequestMapping("/api/users")
@RestController
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<Void> createUser(
            @Valid @RequestBody UserSaveDto userSaveDto,
            UriComponentsBuilder uriComponentsBuilder
    ) {
        Long userId = userService.save(userSaveDto);
        URI location = uriComponentsBuilder.path("/api/users/{userId}")
                .buildAndExpand(userId)
                .toUri();

        return ResponseEntity
                .created(location)
                .build();
    }

    @Operation(summary = "로그인 사용자 정보 조회")
    @GetMapping("/me")
    public ResponseEntity<UserProfileDto> showUserProfile(@AuthenticationPrincipal OAuth2User loginUser) {
        String loginId = loginUser.getName();
        return ResponseEntity
                .ok(userService.getLoginUserDetails(loginId));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDetailsDto> showUserByUserId(@PathVariable Long userId) {
        return ResponseEntity
                .ok(userService.findByUserId(userId));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Void> update(
            @PathVariable Long userId,
            @Valid @RequestBody UserUpdateDto userUpdateDto
    ) {
        userService.update(userId, userUpdateDto);

        return ResponseEntity
                .noContent()
                .build();
    }
}
