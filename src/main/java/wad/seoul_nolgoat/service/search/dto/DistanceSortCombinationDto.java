package wad.seoul_nolgoat.service.search.dto;

import lombok.Getter;
import lombok.Setter;
import wad.seoul_nolgoat.service.tMap.dto.WalkRouteInfoDto;

@Getter
public class DistanceSortCombinationDto {

    private StoreForDistanceSortDto firstStore;
    private StoreForDistanceSortDto secondStore;
    private StoreForDistanceSortDto thirdStore;
    private WalkRouteInfoDto walkRouteInfoDto;

    @Setter
    private double totaleDistnace;

    public DistanceSortCombinationDto(
            StoreForDistanceSortDto firstStore,
            StoreForDistanceSortDto secondStore,
            StoreForDistanceSortDto thirdStore,
            WalkRouteInfoDto walkRouteInfoDto) {
        this.firstStore = firstStore;
        this.secondStore = secondStore;
        this.thirdStore = thirdStore;
        this.walkRouteInfoDto = walkRouteInfoDto;
    }

    public DistanceSortCombinationDto(
            StoreForDistanceSortDto firstStore,
            StoreForDistanceSortDto secondStore,
            StoreForDistanceSortDto thirdStore) {
        this.firstStore = firstStore;
        this.secondStore = secondStore;
        this.thirdStore = thirdStore;
    }

    public DistanceSortCombinationDto(StoreForDistanceSortDto firstStore, StoreForDistanceSortDto secondStore) {
        this.firstStore = firstStore;
        this.secondStore = secondStore;
    }

    public DistanceSortCombinationDto(StoreForDistanceSortDto firstStore) {
        this.firstStore = firstStore;
    }
}
