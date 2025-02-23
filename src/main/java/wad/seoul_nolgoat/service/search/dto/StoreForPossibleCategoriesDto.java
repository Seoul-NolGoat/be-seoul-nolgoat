package wad.seoul_nolgoat.service.search.dto;

import wad.seoul_nolgoat.domain.store.StoreType;

public record StoreForPossibleCategoriesDto(StoreType storeType, String category) {
}
