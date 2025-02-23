package wad.seoul_nolgoat.service.search.dto;

import wad.seoul_nolgoat.service.search.SearchService;
import wad.seoul_nolgoat.web.search.dto.CoordinateDto;

import java.util.List;

public record SortConditionDto<T>(
        CoordinateDto startCoordinate,
        List<T> firstFilteredStores,
        List<T> secondFilteredStores,
        List<T> thirdFilteredStores,
        int totalRounds
) {
    public SortConditionDto(
            CoordinateDto startCoordinate,
            List<T> firstFilteredStores,
            List<T> secondFilteredStores,
            List<T> thirdFilteredStores
    ) {
        this(
                startCoordinate,
                firstFilteredStores,
                secondFilteredStores,
                thirdFilteredStores,
                SearchService.THREE_ROUND
        );
    }

    public SortConditionDto(
            CoordinateDto startCoordinate,
            List<T> firstFilteredStores,
            List<T> secondFilteredStores
    ) {
        this(
                startCoordinate,
                firstFilteredStores,
                secondFilteredStores,
                null,
                SearchService.TWO_ROUND
        );
    }

    public SortConditionDto(CoordinateDto startCoordinate, List<T> firstFilteredStores) {
        this(
                startCoordinate,
                firstFilteredStores,
                null,
                null,
                SearchService.ONE_ROUND
        );
    }
}
