package wad.seoul_nolgoat.web.bookmark;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import wad.seoul_nolgoat.service.bookmark.BookmarkService;

import java.net.URI;

@RequiredArgsConstructor
@RequestMapping("/api/bookmarks")
@RestController
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @PostMapping("/{userId}/{storeId}")
    public ResponseEntity<Void> createBookmark(
            @PathVariable Long userId,
            @PathVariable Long storeId,
            UriComponentsBuilder uriComponentsBuilder
    ) {
        Long bookmarkId = bookmarkService.save(userId, storeId);
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
