package wad.seoul_nolgoat.web.store;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wad.seoul_nolgoat.service.bookmark.BookmarkService;
import wad.seoul_nolgoat.service.store.StoreService;
import wad.seoul_nolgoat.web.store.dto.response.StoreDetailsDto;
import wad.seoul_nolgoat.web.store.dto.response.StoreForBookmarkDto;

import java.util.List;

@Tag(name = "가게")
@RequiredArgsConstructor
@RequestMapping("/api/stores")
@RestController
public class StoreController {

    private final StoreService storeService;
    private final BookmarkService bookmarkService;

    @Operation(summary = "가게 단건 조회")
    @GetMapping("/{storeId}")
    public ResponseEntity<StoreDetailsDto> showStoreByStoreId(@PathVariable Long storeId) {
        return ResponseEntity
                .ok(storeService.findStoreWithReviewsByStoreId(storeId));
    }

    @Operation(summary = "유저 ID를 통한 즐겨찾기 가게 목록 조회")
    @GetMapping("/bookmarked/{userId}")
    public ResponseEntity<List<StoreForBookmarkDto>> showBookmarkedStoresByUserId(@PathVariable Long userId) {
        return ResponseEntity
                .ok(bookmarkService.findBookmarkedStoresByUserId(userId));
    }
}
