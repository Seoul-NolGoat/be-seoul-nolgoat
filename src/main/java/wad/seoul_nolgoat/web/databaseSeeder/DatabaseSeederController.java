package wad.seoul_nolgoat.web.databaseSeeder;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wad.seoul_nolgoat.domain.store.StoreType;
import wad.seoul_nolgoat.service.databaseSeeder.DatabaseSeederService;

@Hidden
@RequiredArgsConstructor
@RequestMapping("/seeder")
@RestController
public class DatabaseSeederController {

    private final DatabaseSeederService databaseSeederService;

    @GetMapping("/restaurant")
    public ResponseEntity<Void> seedRestaurantInitialData() {
        databaseSeederService.seedInitialStoreData(StoreType.RESTAURANT);
        return ResponseEntity
                .ok()
                .build();
    }

    @GetMapping("/cafe")
    public ResponseEntity<Void> seedCafeInitialData() {
        databaseSeederService.seedInitialStoreData(StoreType.CAFE);
        return ResponseEntity
                .ok()
                .build();
    }

    @GetMapping("/pcroom")
    public ResponseEntity<Void> seedPcroomInitialData() {
        databaseSeederService.seedInitialStoreData(StoreType.PCROOM);
        return ResponseEntity
                .ok()
                .build();
    }

    @GetMapping("/karaoke")
    public ResponseEntity<Void> seedKaraokeInitialData() {
        databaseSeederService.seedInitialStoreData(StoreType.KARAOKE);
        return ResponseEntity
                .ok()
                .build();
    }

    @GetMapping("/billiard")
    public ResponseEntity<Void> seedBilliardInitialData() {
        databaseSeederService.seedInitialStoreData(StoreType.BILLIARD);
        return ResponseEntity
                .ok()
                .build();
    }
}
