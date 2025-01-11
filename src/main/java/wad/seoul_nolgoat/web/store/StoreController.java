package wad.seoul_nolgoat.web.store;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wad.seoul_nolgoat.service.store.StoreService;
import wad.seoul_nolgoat.web.store.dto.response.StoreDetailsDto;
import wad.seoul_nolgoat.web.store.dto.response.StoreListDto;

@Tag(name = "가게")
@RequiredArgsConstructor
@RequestMapping("/api/stores")
@RestController
public class StoreController {

    private final StoreService storeService;

    @Operation(summary = "가게 단건 조회")
    @GetMapping("/{storeId}")
    public ResponseEntity<StoreDetailsDto> showStoreByStoreId(@PathVariable Long storeId) {
        return ResponseEntity
                .ok(storeService.findStoreWithReviewsByStoreId(storeId));
    }

    @GetMapping("/search")
    public Page<StoreListDto> searchStores(
            @RequestParam String searchInput,
            @RequestParam int page,
            @RequestParam int size
    ) {
        return storeService.searchStores(searchInput, page, size);
    }
}
