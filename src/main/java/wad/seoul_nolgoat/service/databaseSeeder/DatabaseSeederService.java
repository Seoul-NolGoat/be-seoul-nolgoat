package wad.seoul_nolgoat.service.databaseSeeder;

import com.google.common.util.concurrent.RateLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import wad.seoul_nolgoat.domain.store.Store;
import wad.seoul_nolgoat.domain.store.StoreRepository;
import wad.seoul_nolgoat.domain.store.StoreType;
import wad.seoul_nolgoat.service.kakaoMap.KakaoMapService;
import wad.seoul_nolgoat.service.kakaoMap.dto.CoordinateDto;
import wad.seoul_nolgoat.service.kakaoMap.dto.StoreAdditionalInfoDto;
import wad.seoul_nolgoat.service.seoulStore.SeoulStoreService;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RequiredArgsConstructor
@Service
public class DatabaseSeederService {

    private static final double RATE_LIMIT = 50.0;
    private static final int INITIAL_START_IDX = 1;
    private static final int INITIAL_END_IDX = 1000;
    private static final int INCREMENT = 1000;
    private static final int LOG_INTERVAL = 10000;

    private final SeoulStoreService seoulStoreService;
    private final KakaoMapService kakaoMapService;
    private final StoreRepository storeRepository;

    private final RateLimiter rateLimiter = RateLimiter.create(RATE_LIMIT); // 초당 50회 호출 제한

    public void seedInitialStoreData(StoreType storeType) {
        int startIdx = INITIAL_START_IDX;
        int endIdx = INITIAL_END_IDX;

        while (true) {
            try {
                List<Store> stores;
                Optional<List<Store>> storesOptional = seoulStoreService.fetchSeoulStoreInfo(storeType, startIdx, endIdx);
                if (storesOptional.isPresent()) {
                    stores = storesOptional.get();
                } else {
                    break;
                }

                List<CompletableFuture<Void>> coordinateFutures = stores.stream()
                        .map(this::updateCoordinatesAsync)
                        .toList();
                CompletableFuture.allOf(coordinateFutures.toArray(new CompletableFuture[0])).join();

                List<CompletableFuture<Void>> infoFutures = stores.stream()
                        .map(this::updateRestaurantInfoAsync)
                        .toList();
                CompletableFuture.allOf(infoFutures.toArray(new CompletableFuture[0])).join();

                List<CompletableFuture<Void>> ratingFutures = stores.stream()
                        .map(this::updateRestaurantRatingAsync)
                        .toList();
                CompletableFuture.allOf(ratingFutures.toArray(new CompletableFuture[0])).join();

                storeRepository.saveAll(stores);

                if (endIdx % LOG_INTERVAL == 0) {
                    log.info("Saved stores up to index {}", endIdx);
                }
                startIdx += INCREMENT;
                endIdx += INCREMENT;

            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
    }

    @Async
    public CompletableFuture<Void> updateCoordinatesAsync(Store store) {
        return CompletableFuture.runAsync(() -> {
            rateLimiter.acquire(); // RateLimiter 사용
            if (store.getRoadAddress() != null && !store.getRoadAddress().isEmpty()) {
                CoordinateDto coordinates = kakaoMapService.fetchCoordinate(store.getRoadAddress());
                if (coordinates != null) {
                    store.updateCoordinates(coordinates.getLatitude(), coordinates.getLongitude());
                }
            }
        });
    }

    @Async
    public CompletableFuture<Void> updateRestaurantInfoAsync(Store store) {
        return CompletableFuture.runAsync(() -> {
            rateLimiter.acquire(); // RateLimiter 사용
            if (store.getLongitude() != 0 && store.getLatitude() != 0) {
                StoreAdditionalInfoDto storeAdditionalInfoDto = kakaoMapService.fetchStoreAdditionalInfo(store.getName(), store.getLongitude(), store.getLatitude());
                if (storeAdditionalInfoDto != null) {
                    store.updateAdditionalInfo(storeAdditionalInfoDto.getCategory(), storeAdditionalInfoDto.getPhoneNumber(), storeAdditionalInfoDto.getPlaceUrl());
                }
            }
        });
    }

    @Async
    public CompletableFuture<Void> updateRestaurantRatingAsync(Store store) {
        return CompletableFuture.runAsync(() -> {
            if (store.getPlaceUrl() != null) {
                try {
                    Long restaurantId = Long.parseLong(store.getPlaceUrl().substring(store.getPlaceUrl().lastIndexOf('/') + 1));
                    Double kakaoGrade = kakaoMapService.fetchStoreKakaoGrade(restaurantId);
                    if (kakaoGrade != null) {
                        store.updateKakaoAverageGrade(kakaoGrade);
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
