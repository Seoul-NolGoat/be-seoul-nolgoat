package wad.seoul_nolgoat.web.search.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import wad.seoul_nolgoat.domain.store.Store;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class CombinationDto {

    private final List<Store> stores;
}
