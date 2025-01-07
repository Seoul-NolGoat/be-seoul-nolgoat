package wad.seoul_nolgoat.web.user;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import wad.seoul_nolgoat.service.bookmark.BookmarkService;
import wad.seoul_nolgoat.service.review.ReviewService;
import wad.seoul_nolgoat.service.user.UserService;
import wad.seoul_nolgoat.web.bookmark.dto.response.StoreForBookmarkDto;
import wad.seoul_nolgoat.web.review.dto.response.ReviewDetailsForUserDto;
import wad.seoul_nolgoat.web.user.dto.request.UserSaveDto;
import wad.seoul_nolgoat.web.user.dto.request.UserUpdateDto;
import wad.seoul_nolgoat.web.user.dto.response.UserProfileDto;

import java.net.URI;

@Tag(name = "유저")
@RequiredArgsConstructor
@RequestMapping("/api/users")
@RestController
public class UserController {

    private final UserService userService;
    private final BookmarkService bookmarkService;
    private final ReviewService reviewService;

    @Hidden
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

    @Hidden
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

    @Operation(summary = "내가 등록한 즐겨찾기 가게 목록 조회")
    @GetMapping("/me/bookmarks")
    public ResponseEntity<Page<StoreForBookmarkDto>> showMyBookmarkedStores(
            @AuthenticationPrincipal OAuth2User loginUser,
            Pageable pageable
    ) {
        return ResponseEntity
                .ok(bookmarkService.findBookmarkedStoresByLoginId(loginUser.getName(), pageable));
    }

    @Operation(summary = "내가 작성한 리뷰 목록 조회")
    @GetMapping("/me/reviews")
    public ResponseEntity<Page<ReviewDetailsForUserDto>> showMyReviews(
            @AuthenticationPrincipal OAuth2User loginUser,
            Pageable pageable
    ) {
        return ResponseEntity
                .ok(reviewService.findReviewDetailsByLoginId(loginUser.getName(), pageable));
    }
}
