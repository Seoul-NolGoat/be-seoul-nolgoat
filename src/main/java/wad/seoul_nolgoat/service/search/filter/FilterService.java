package wad.seoul_nolgoat.service.search.filter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import wad.seoul_nolgoat.domain.store.StoreRepository;
import wad.seoul_nolgoat.domain.store.StoreType;
import wad.seoul_nolgoat.service.search.dto.StoreForDistanceSortDto;
import wad.seoul_nolgoat.service.search.dto.StoreForGradeSortDto;
import wad.seoul_nolgoat.web.search.dto.CoordinateDto;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class FilterService {

    private static final Set<String> NON_RESTAURANT_CATEGORIES = Set.of(
            StoreType.CAFE.name(),
            StoreType.PCROOM.name(),
            StoreType.KARAOKE.name(),
            StoreType.BILLIARD.name()
    );

    private final StoreRepository storeRepository;

    public List<StoreForDistanceSortDto> filterByRadiusRangeAndCategoryForDistanceSort(
            CoordinateDto startCoordinate,
            double radiusRange,
            String category
    ) {
        if (NON_RESTAURANT_CATEGORIES.contains(category)) {
            return storeRepository.findByRadiusRangeAndStoreTypeForDistanceSort(
                    startCoordinate,
                    radiusRange,
                    StoreType.getStoreTypeByName(category)
            );
        }
        return storeRepository.findByRadiusRangeAndCategoryForDistanceSort(
                startCoordinate,
                radiusRange,
                category
        );
    }

    public List<StoreForGradeSortDto> filterByRadiusRangeAndCategoryForKakaoGradeSort(
            CoordinateDto startCoordinate,
            double radiusRange,
            String category
    ) {
        if (NON_RESTAURANT_CATEGORIES.contains(category)) {
            return storeRepository.findByRadiusRangeAndStoreTypeForKakaoGradeSort(
                    startCoordinate,
                    radiusRange,
                    StoreType.getStoreTypeByName(category)
            );
        }
        return storeRepository.findByRadiusRangeAndCategoryForKakaoGradeSort(
                startCoordinate,
                radiusRange,
                category
        );
    }

    public List<StoreForGradeSortDto> filterByRadiusRangeAndCategoryForNolgoatGradeSort(
            CoordinateDto startCoordinate,
            double radiusRange,
            String category
    ) {
        if (NON_RESTAURANT_CATEGORIES.contains(category)) {
            return storeRepository.findByRadiusRangeAndStoreTypeForNolgoatGradeSort(
                    startCoordinate,
                    radiusRange,
                    StoreType.getStoreTypeByName(category)
            );
        }
        return storeRepository.findByRadiusRangeAndCategoryForNolgoatGradeSort(
                startCoordinate,
                radiusRange,
                category
        );
    }

    public List<String> findCategoriesByRadiusRange(CoordinateDto startCoordinate, double radiusRange) {
        return storeRepository.findCategoriesByRadiusRange(startCoordinate, radiusRange);
    }
}
