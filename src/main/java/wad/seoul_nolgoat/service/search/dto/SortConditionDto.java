package wad.seoul_nolgoat.service.search.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import wad.seoul_nolgoat.web.search.dto.CoordinateDto;

import java.util.List;

@Getter
@RequiredArgsConstructor
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
        this.totalRounds = 3;
    }

    public SortConditionDto(
            CoordinateDto startCoordinate,
            List<T> firstFilteredStores,
            List<T> secondFilteredStores) {
        this.startCoordinate = startCoordinate;
        this.firstFilteredStores = firstFilteredStores;
        this.secondFilteredStores = secondFilteredStores;
        this.totalRounds = 2;
    }

    public SortConditionDto(CoordinateDto startCoordinate, List<T> firstFilteredStores) {
        this.startCoordinate = startCoordinate;
        this.firstFilteredStores = firstFilteredStores;
        this.totalRounds = 1;
    }

}
