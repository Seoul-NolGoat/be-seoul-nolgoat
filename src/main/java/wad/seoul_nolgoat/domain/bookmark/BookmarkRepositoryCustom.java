package wad.seoul_nolgoat.domain.bookmark;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import wad.seoul_nolgoat.web.bookmark.dto.response.StoreDetailsForBookmarkDto;

public interface BookmarkRepositoryCustom {

    Page<StoreDetailsForBookmarkDto> findBookmarkedStoresByLoginId(String loginId, Pageable pageable);
}
