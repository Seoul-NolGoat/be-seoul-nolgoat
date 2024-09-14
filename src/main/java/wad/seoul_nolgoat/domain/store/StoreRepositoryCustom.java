package wad.seoul_nolgoat.domain.store;

import wad.seoul_nolgoat.service.search.dto.StoreForDistanceSortDto;
import wad.seoul_nolgoat.service.search.dto.StoreForGradeSortDto;
import wad.seoul_nolgoat.web.search.dto.CoordinateDto;

import java.util.List;

public interface StoreRepositoryCustom {

    List<StoreForDistanceSortDto> findByRadiusRangeAndCategoryForDistanceSort(
            CoordinateDto startCoordinate,
            double radiusRange,
            String category
    );

    List<StoreForDistanceSortDto> findByRadiusRangeAndStoreTypeForDistanceSort(
            CoordinateDto startCoordinate,
            double radiusRange,
            StoreType storeType
    );

    List<StoreForGradeSortDto> findByRadiusRangeAndCategoryForKakaoGradeSort(
            CoordinateDto startCoordinate,
            double radiusRange,
            String category
    );

    List<StoreForGradeSortDto> findByRadiusRangeAndStoreTypeForKakaoGradeSort(
            CoordinateDto startCoordinate,
            double radiusRange,
            StoreType storeType
    );

    List<StoreForGradeSortDto> findByRadiusRangeAndCategoryForNolgoatGradeSort(
            CoordinateDto startCoordinate,
            double radiusRange,
            String category
    );

    List<StoreForGradeSortDto> findByRadiusRangeAndStoreTypeForNolgoatGradeSort(
            CoordinateDto startCoordinate,
            double radiusRange,
            StoreType storeType
    );

    List<String> findCategoriesByRadiusRange(CoordinateDto startCoordinate, double radiusRange);
}
