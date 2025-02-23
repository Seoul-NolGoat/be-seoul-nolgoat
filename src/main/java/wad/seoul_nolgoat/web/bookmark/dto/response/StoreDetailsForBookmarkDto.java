package wad.seoul_nolgoat.web.bookmark.dto.response;

import wad.seoul_nolgoat.domain.store.StoreType;

public record StoreDetailsForBookmarkDto(
        Long bookmarkId,
        Long storeId,
        StoreType storeType,
        String name,
        String lotAddress,
        String roadAddress,
        double kakaoAverageGrade,
        double nolgoatAverageGrade
) {
}
