package wad.seoul_nolgoat.web.search.dto.response;

import lombok.Getter;
import lombok.Setter;
import wad.seoul_nolgoat.service.tMap.dto.WalkRouteInfoDto;
import wad.seoul_nolgoat.web.store.dto.response.StoreDetailsForCombinationDto;

@Getter
public class CombinationDto {

    private StoreDetailsForCombinationDto firstStore;
    private StoreDetailsForCombinationDto secondStore;
    private StoreDetailsForCombinationDto thirdStore;

    @Setter
    private WalkRouteInfoDto walkRouteInfoDto;

    public CombinationDto(
            StoreDetailsForCombinationDto firstStore,
            StoreDetailsForCombinationDto secondStore,
            StoreDetailsForCombinationDto thirdStore,
            WalkRouteInfoDto walkRouteInfoDto
    ) {
        this.firstStore = firstStore;
        this.secondStore = secondStore;
        this.thirdStore = thirdStore;
        this.walkRouteInfoDto = walkRouteInfoDto;
    }

    public CombinationDto(
            StoreDetailsForCombinationDto firstStore,
            StoreDetailsForCombinationDto secondStore,
            StoreDetailsForCombinationDto thirdStore
    ) {
        this.firstStore = firstStore;
        this.secondStore = secondStore;
        this.thirdStore = thirdStore;
    }

    public CombinationDto(
            StoreDetailsForCombinationDto firstStore,
            StoreDetailsForCombinationDto secondStore,
            WalkRouteInfoDto walkRouteInfoDto
    ) {
        this.firstStore = firstStore;
        this.secondStore = secondStore;
        this.walkRouteInfoDto = walkRouteInfoDto;
    }

    public CombinationDto(StoreDetailsForCombinationDto firstStore, StoreDetailsForCombinationDto secondStore) {
        this.firstStore = firstStore;
        this.secondStore = secondStore;
    }

    public CombinationDto(StoreDetailsForCombinationDto firstStore, WalkRouteInfoDto walkRouteInfoDto) {
        this.firstStore = firstStore;
        this.walkRouteInfoDto = walkRouteInfoDto;
    }

    public CombinationDto(StoreDetailsForCombinationDto firstStore) {
        this.firstStore = firstStore;
    }
}
