package wad.seoul_nolgoat.domain.store;

import wad.seoul_nolgoat.service.search.dto.StoreForDistanceSortDto;
import wad.seoul_nolgoat.service.search.dto.StoreForGradeSortDto;
import wad.seoul_nolgoat.service.search.dto.StoreForPossibleCategoriesDto;
import wad.seoul_nolgoat.web.search.dto.CoordinateDto;
import wad.seoul_nolgoat.web.store.dto.response.StoreDetailsDto;

import java.util.List;
import java.util.Optional;

public interface StoreRepositoryCustom {

    Optional<StoreDetailsDto> findStoreWithReviewsByStoreId(Long storeId);

    List<StoreForDistanceSortDto> findByRadiusRangeAndCategoryForDistanceSort(
            CoordinateDto startCoordinate,
            double radiusRange,
            String category
    );

    List<StoreForDistanceSortDto> findByRadiusRangeAndCategoryAndStoreTypeForDistanceSort(
            CoordinateDto startCoordinate,
            double radiusRange,
            String category
    );

    List<StoreForGradeSortDto> findByRadiusRangeAndCategoryForKakaoGradeSort(
            CoordinateDto startCoordinate,
            double radiusRange,
            String category
    );

    List<StoreForGradeSortDto> findByRadiusRangeAndCategoryAndStoreTypeForKakaoGradeSort(
            CoordinateDto startCoordinate,
            double radiusRange,
            String category
    );

    List<StoreForGradeSortDto> findByRadiusRangeAndCategoryForNolgoatGradeSort(
            CoordinateDto startCoordinate,
            double radiusRange,
            String category
    );

    List<StoreForGradeSortDto> findByRadiusRangeAndCategoryAndStoreTypeForNolgoatGradeSort(
            CoordinateDto startCoordinate,
            double radiusRange,
            String category
    );

    List<StoreForPossibleCategoriesDto> findCategoriesByRadiusRange(CoordinateDto startCoordinate, double radiusRange);
}
