package wad.seoul_nolgoat.service.search.dto;

import lombok.Getter;
import wad.seoul_nolgoat.service.search.SearchService;

@Getter
public class GradeSortCombinationDto {

    private StoreForGradeSortDto firstStore;
    private StoreForGradeSortDto secondStore;
    private StoreForGradeSortDto thirdStore;
    private final int totalRounds;

    public GradeSortCombinationDto(
            StoreForGradeSortDto firstStore,
            StoreForGradeSortDto secondStore,
            StoreForGradeSortDto thirdStore) {
        this.firstStore = firstStore;
        this.secondStore = secondStore;
        this.thirdStore = thirdStore;
        this.totalRounds = SearchService.THREE_ROUND;
    }

    public GradeSortCombinationDto(StoreForGradeSortDto firstStore, StoreForGradeSortDto secondStore) {
        this.firstStore = firstStore;
        this.secondStore = secondStore;
        this.totalRounds = SearchService.TWO_ROUND;
    }

    public GradeSortCombinationDto(StoreForGradeSortDto firstStore) {
        this.firstStore = firstStore;
        this.totalRounds = SearchService.ONE_ROUND;
    }
}
