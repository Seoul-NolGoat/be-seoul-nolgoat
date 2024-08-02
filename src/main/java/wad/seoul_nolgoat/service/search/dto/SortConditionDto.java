package wad.seoul_nolgoat.service.search.dto;

import lombok.Getter;
import wad.seoul_nolgoat.service.search.SearchService;
import wad.seoul_nolgoat.web.search.dto.CoordinateDto;

import java.util.List;

@Getter
public class SortConditionDto<T> {

    private final CoordinateDto startCoordinate;
    private List<T> firstFilteredStores;
    private List<T> secondFilteredStores;
    private List<T> thirdFilteredStores;
    private final int totalRounds;

    public SortConditionDto(
            CoordinateDto startCoordinate,
            List<T> firstFilteredStores,
            List<T> secondFilteredStores,
            List<T> thirdFilteredStores) {
        this.startCoordinate = startCoordinate;
        this.firstFilteredStores = firstFilteredStores;
        this.secondFilteredStores = secondFilteredStores;
        this.thirdFilteredStores = thirdFilteredStores;
        this.totalRounds = SearchService.THREE_ROUND;
    }

    public SortConditionDto(
            CoordinateDto startCoordinate,
            List<T> firstFilteredStores,
            List<T> secondFilteredStores) {
        this.startCoordinate = startCoordinate;
        this.firstFilteredStores = firstFilteredStores;
        this.secondFilteredStores = secondFilteredStores;
        this.totalRounds = SearchService.TWO_ROUND;
    }

    public SortConditionDto(CoordinateDto startCoordinate, List<T> firstFilteredStores) {
        this.startCoordinate = startCoordinate;
        this.firstFilteredStores = firstFilteredStores;
        this.totalRounds = SearchService.ONE_ROUND;
    }
}
