package wad.seoul_nolgoat.service.search.dto;

import lombok.Getter;

@Getter
public class GradeSortCombinationDto {

    private StoreForGradeSortDto firstStore;
    private StoreForGradeSortDto secondStore;
    private StoreForGradeSortDto thirdStore;

    public GradeSortCombinationDto(
            StoreForGradeSortDto firstStore,
            StoreForGradeSortDto secondStore,
            StoreForGradeSortDto thirdStore) {
        this.firstStore = firstStore;
        this.secondStore = secondStore;
        this.thirdStore = thirdStore;
    }

    public GradeSortCombinationDto(StoreForGradeSortDto firstStore, StoreForGradeSortDto secondStore) {
        this.firstStore = firstStore;
        this.secondStore = secondStore;
    }

    public GradeSortCombinationDto(StoreForGradeSortDto firstStore) {
        this.firstStore = firstStore;
    }
}
