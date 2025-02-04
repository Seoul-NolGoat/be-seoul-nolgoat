package wad.seoul_nolgoat.web.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wad.seoul_nolgoat.service.bookmark.BookmarkService;
import wad.seoul_nolgoat.service.comment.CommentService;
import wad.seoul_nolgoat.service.review.ReviewService;
import wad.seoul_nolgoat.service.user.UserService;
import wad.seoul_nolgoat.web.bookmark.dto.response.StoreDetailsForBookmarkDto;
import wad.seoul_nolgoat.web.comment.dto.response.CommentDetailsForUserDto;
import wad.seoul_nolgoat.web.review.dto.response.ReviewDetailsForUserDto;
import wad.seoul_nolgoat.web.user.dto.response.UserProfileDto;

@Tag(name = "유저")
@RequiredArgsConstructor
@RequestMapping("/api/users")
@RestController
public class UserController {

    private final UserService userService;
    private final BookmarkService bookmarkService;
    private final ReviewService reviewService;
    private final CommentService commentService;

    @Operation(summary = "로그인 유저 정보 조회")
    @GetMapping("/me")
    public ResponseEntity<UserProfileDto> showUserProfile(@AuthenticationPrincipal OAuth2User loginUser) {
        String loginId = loginUser.getName();
        return ResponseEntity
                .ok(userService.getLoginUserDetails(loginId));
    }

    @Operation(summary = "내가 등록한 즐겨찾기 가게 목록 조회")
    @GetMapping("/me/bookmarks")
    public ResponseEntity<Page<StoreDetailsForBookmarkDto>> showMyBookmarkedStores(
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

    @Operation(summary = "내가 작성한 댓글 목록 조회")
    @GetMapping("/me/comments")
    public ResponseEntity<Page<CommentDetailsForUserDto>> showMyComments(
            @AuthenticationPrincipal OAuth2User loginUser,
            Pageable pageable
    ) {
        return ResponseEntity
                .ok(commentService.findCommentsByLoginId(loginUser.getName(), pageable));
    }
}
