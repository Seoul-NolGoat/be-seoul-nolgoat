package wad.seoul_nolgoat.web.search.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import wad.seoul_nolgoat.web.store.dto.response.StoreForCombinationDto;

@Getter
@RequiredArgsConstructor
public class CombinationDto {

    private final StoreForCombinationDto firstStore;
    private final StoreForCombinationDto secondStore;
    private final StoreForCombinationDto thirdStore;
}
