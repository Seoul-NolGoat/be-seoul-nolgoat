package wad.seoul_nolgoat.web.bookmark.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import wad.seoul_nolgoat.domain.store.StoreType;

@Getter
@RequiredArgsConstructor
public class StoreDetailsForBookmarkDto {

    private final Long bookmarkId;
    private final Long storeId;
    private final StoreType storeType;
    private final String name;
    private final String lotAddress;
    private final String roadAddress;
    private final double kakaoAverageGrade;
    private final double nolgoatAverageGrade;
}
