package wad.seoul_nolgoat.web.bookmark;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import wad.seoul_nolgoat.service.bookmark.BookmarkService;

import java.net.URI;

@Tag(name = "즐겨찾기")
@RequiredArgsConstructor
@RequestMapping("/api/bookmarks")
@RestController
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @Operation(summary = "즐겨찾기 등록")
    @PostMapping("/{storeId}")
    public ResponseEntity<Void> createBookmark(
            @AuthenticationPrincipal OAuth2User loginUser,
            @PathVariable Long storeId,
            UriComponentsBuilder uriComponentsBuilder
    ) {
        Long bookmarkId = bookmarkService.save(loginUser.getName(), storeId);
        URI location = uriComponentsBuilder.path("/api/bookmarks/{bookmarkId}")
                .buildAndExpand(bookmarkId)
                .toUri();

        return ResponseEntity
                .created(location)
                .build();
    }

    @Operation(summary = "즐겨찾기 삭제")
    @DeleteMapping("/{userId}/{storeId}")
    public ResponseEntity<Void> delete(
            @AuthenticationPrincipal OAuth2User loginUser,
            @PathVariable Long userId,
            @PathVariable Long storeId
    ) {
        bookmarkService.delete(
                loginUser.getName(),
                userId,
                storeId
        );

        return ResponseEntity
                .noContent()
                .build();
    }

    @Operation(summary = "즐겨찾기 여부 확인")
    @GetMapping("/{userId}/{storeId}")
    public ResponseEntity<Boolean> checkIfBookmarked(@PathVariable Long userId, @PathVariable Long storeId) {
        return ResponseEntity
                .ok(bookmarkService.checkIfBookmarked(userId, storeId));
    }
}
