package wad.seoul_nolgoat.web.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import wad.seoul_nolgoat.service.user.UserService;
import wad.seoul_nolgoat.web.user.dto.request.UserSaveDto;
import wad.seoul_nolgoat.web.user.dto.request.UserUpdateDto;
import wad.seoul_nolgoat.web.user.dto.response.UserDetailsDto;

import java.net.URI;

@RequiredArgsConstructor
@RequestMapping("/api/users")
@RestController
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<?> createUser(
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

    @GetMapping("/{userId}")
    public ResponseEntity<UserDetailsDto> showUserByUserId(@PathVariable Long userId) {
        return ResponseEntity
                .ok(userService.findByUserId(userId));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> update(
            @PathVariable Long userId,
            @Valid @RequestBody UserUpdateDto userUpdateDto
    ) {
        userService.update(userId, userUpdateDto);

        return ResponseEntity
                .noContent()
                .build();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteById(@PathVariable Long userId) {
        userService.deleteById(userId);

        return ResponseEntity
                .noContent()
                .build();
    }
}
