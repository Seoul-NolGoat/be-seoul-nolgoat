package wad.seoul_nolgoat.web.bookmark;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import wad.seoul_nolgoat.service.bookmark.BookmarkService;

import java.net.URI;

@RequiredArgsConstructor
@RequestMapping("/api/bookmarks")
@RestController
public class BookmarkController {

    private final BookmarkService bookmarkService;

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

    @DeleteMapping("/{userId}/{storeId}")
    public ResponseEntity<Void> deleteById(@PathVariable Long userId, @PathVariable Long storeId) {
        bookmarkService.deleteById(userId, storeId);

        return ResponseEntity
                .noContent()
                .build();
    }

    @GetMapping("/{userId}/{storeId}")
    public ResponseEntity<Boolean> checkIfBookmarked(@PathVariable Long userId, @PathVariable Long storeId) {
        return ResponseEntity
                .ok(bookmarkService.checkIfBookmarked(userId, storeId));
    }
}
