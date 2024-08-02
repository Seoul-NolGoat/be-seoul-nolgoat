package wad.seoul_nolgoat.web.search.dto.response;

import lombok.Getter;
import wad.seoul_nolgoat.web.store.dto.response.StoreForCombinationDto;

@Getter
public class CombinationDto {

    private StoreForCombinationDto firstStore;
    private StoreForCombinationDto secondStore;
    private StoreForCombinationDto thirdStore;

    public CombinationDto(
            StoreForCombinationDto firstStore,
            StoreForCombinationDto secondStore,
            StoreForCombinationDto thirdStore) {
        this.firstStore = firstStore;
        this.secondStore = secondStore;
        this.thirdStore = thirdStore;
    }

    public CombinationDto(StoreForCombinationDto firstStore, StoreForCombinationDto secondStore) {
        this.firstStore = firstStore;
        this.secondStore = secondStore;
    }

    public CombinationDto(StoreForCombinationDto firstStore) {
        this.firstStore = firstStore;
    }
}
