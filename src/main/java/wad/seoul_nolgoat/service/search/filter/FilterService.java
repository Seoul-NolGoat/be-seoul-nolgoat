package wad.seoul_nolgoat.service.search.filter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import wad.seoul_nolgoat.domain.store.StoreRepository;
import wad.seoul_nolgoat.service.search.dto.StoreForDistanceSortDto;
import wad.seoul_nolgoat.service.search.dto.StoreForGradeSortDto;
import wad.seoul_nolgoat.web.search.dto.CoordinateDto;

import java.util.List;

@RequiredArgsConstructor
@Service
public class FilterService {

    private final StoreRepository storeRepository;

    public List<StoreForDistanceSortDto> filterByRadiusRangeAndCategoryForDistanceSort(
            CoordinateDto startCoordinate,
            double radiusRange,
            String category) {
        return storeRepository.findByRadiusRangeAndCategoryForDistanceSort(
                startCoordinate,
                radiusRange,
                category
        );
    }

    public List<StoreForGradeSortDto> filterByRadiusRangeAndCategoryForKakaoGradeSort(
            CoordinateDto startCoordinate,
            double radiusRange,
            String category) {
        return storeRepository.findByRadiusRangeAndCategoryForKakaoGradeSort(
                startCoordinate,
                radiusRange,
                category
        );
    }

    public List<StoreForGradeSortDto> filterByRadiusRangeAndCategoryForNolgoatGradeSort(
            CoordinateDto startCoordinate,
            double radiusRange,
            String category) {
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
