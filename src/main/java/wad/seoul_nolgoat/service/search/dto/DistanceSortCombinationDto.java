package wad.seoul_nolgoat.service.search.dto;

import lombok.Getter;
import lombok.Setter;
import wad.seoul_nolgoat.service.search.SearchService;
import wad.seoul_nolgoat.service.tMap.dto.WalkRouteInfoDto;

@Getter
public class DistanceSortCombinationDto {

    private StoreForDistanceSortDto firstStore;
    private StoreForDistanceSortDto secondStore;
    private StoreForDistanceSortDto thirdStore;
    private WalkRouteInfoDto walkRouteInfoDto;
    private final int totalRounds;

    @Setter
    private double totalDistance;

    public DistanceSortCombinationDto(
            StoreForDistanceSortDto firstStore,
            StoreForDistanceSortDto secondStore,
            StoreForDistanceSortDto thirdStore,
            WalkRouteInfoDto walkRouteInfoDto) {
        this.firstStore = firstStore;
        this.secondStore = secondStore;
        this.thirdStore = thirdStore;
        this.walkRouteInfoDto = walkRouteInfoDto;
        this.totalRounds = SearchService.THREE_ROUND;
    }

    public DistanceSortCombinationDto(
            StoreForDistanceSortDto firstStore,
            StoreForDistanceSortDto secondStore,
            WalkRouteInfoDto walkRouteInfoDto) {
        this.firstStore = firstStore;
        this.secondStore = secondStore;
        this.walkRouteInfoDto = walkRouteInfoDto;
        this.totalRounds = SearchService.TWO_ROUND;
    }

    public DistanceSortCombinationDto(StoreForDistanceSortDto firstStore, WalkRouteInfoDto walkRouteInfoDto) {
        this.firstStore = firstStore;
        this.walkRouteInfoDto = walkRouteInfoDto;
        this.totalRounds = SearchService.ONE_ROUND;
    }

    public DistanceSortCombinationDto(
            StoreForDistanceSortDto firstStore,
            StoreForDistanceSortDto secondStore,
            StoreForDistanceSortDto thirdStore) {
        this.firstStore = firstStore;
        this.secondStore = secondStore;
        this.thirdStore = thirdStore;
        this.totalRounds = SearchService.THREE_ROUND;
    }

    public DistanceSortCombinationDto(StoreForDistanceSortDto firstStore, StoreForDistanceSortDto secondStore) {
        this.firstStore = firstStore;
        this.secondStore = secondStore;
        this.totalRounds = SearchService.TWO_ROUND;
    }

    public DistanceSortCombinationDto(StoreForDistanceSortDto firstStore) {
        this.firstStore = firstStore;
        this.totalRounds = SearchService.ONE_ROUND;
    }
}
