package wad.seoul_nolgoat.web.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import wad.seoul_nolgoat.service.user.UserService;
import wad.seoul_nolgoat.web.user.dto.request.UserSaveDto;
import wad.seoul_nolgoat.web.user.dto.request.UserUpdateDto;
import wad.seoul_nolgoat.web.user.dto.response.UserDetailsDto;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RequestMapping("/api/users")
@RestController
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<?> createUser(
            @Valid @RequestBody UserSaveDto userSaveDto,
            BindingResult bindingResult,
            UriComponentsBuilder uriComponentsBuilder
    ) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getAllErrors()
                    .forEach(error ->
                            errors.put(
                                    ((FieldError) error).getField(),
                                    error.getDefaultMessage()
                            )
                    );

            return ResponseEntity
                    .badRequest()
                    .body(errors);
        }
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
            @Valid @RequestBody UserUpdateDto userUpdateDto,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getAllErrors()
                    .forEach(error ->
                            errors.put(
                                    ((FieldError) error).getField(),
                                    error.getDefaultMessage()
                            )
                    );
            
            return ResponseEntity
                    .badRequest()
                    .body(errors);
        }
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
