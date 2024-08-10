package wad.seoul_nolgoat.web.search.dto.response;

import lombok.Getter;
import lombok.Setter;
import wad.seoul_nolgoat.service.tMap.dto.WalkRouteInfoDto;
import wad.seoul_nolgoat.web.store.dto.response.StoreForCombinationDto;

@Getter
public class CombinationDto {

    private StoreForCombinationDto firstStore;
    private StoreForCombinationDto secondStore;
    private StoreForCombinationDto thirdStore;

    @Setter
    private WalkRouteInfoDto walkRouteInfoDto;

    public CombinationDto(
            StoreForCombinationDto firstStore,
            StoreForCombinationDto secondStore,
            StoreForCombinationDto thirdStore,
            WalkRouteInfoDto walkRouteInfoDto) {
        this.firstStore = firstStore;
        this.secondStore = secondStore;
        this.thirdStore = thirdStore;
        this.walkRouteInfoDto = walkRouteInfoDto;
    }

    public CombinationDto(
            StoreForCombinationDto firstStore,
            StoreForCombinationDto secondStore,
            StoreForCombinationDto thirdStore) {
        this.firstStore = firstStore;
        this.secondStore = secondStore;
        this.thirdStore = thirdStore;
    }

    public CombinationDto(
            StoreForCombinationDto firstStore,
            StoreForCombinationDto secondStore,
            WalkRouteInfoDto walkRouteInfoDto) {
        this.firstStore = firstStore;
        this.secondStore = secondStore;
        this.walkRouteInfoDto = walkRouteInfoDto;
    }

    public CombinationDto(StoreForCombinationDto firstStore, StoreForCombinationDto secondStore) {
        this.firstStore = firstStore;
        this.secondStore = secondStore;
    }

    public CombinationDto(StoreForCombinationDto firstStore, WalkRouteInfoDto walkRouteInfoDto) {
        this.firstStore = firstStore;
        this.walkRouteInfoDto = walkRouteInfoDto;
    }

    public CombinationDto(StoreForCombinationDto firstStore) {
        this.firstStore = firstStore;
    }
}
