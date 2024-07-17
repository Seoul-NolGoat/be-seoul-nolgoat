package wad.seoul_nolgoat.web.store;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wad.seoul_nolgoat.service.store.StoreService;
import wad.seoul_nolgoat.web.store.dto.response.StoreDetailsDto;

@RequiredArgsConstructor
@RequestMapping("/api/stores")
@RestController
public class StoreController {

    private final StoreService storeService;

    @GetMapping("/{storeId}")
    public ResponseEntity<StoreDetailsDto> showStoreByStoreId(@PathVariable Long storeId) {
        return ResponseEntity
                .ok(storeService.findByStoreId(storeId));
    }
}
