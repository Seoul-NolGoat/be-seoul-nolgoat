package wad.seoul_nolgoat.service.bookmark;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wad.seoul_nolgoat.domain.bookmark.Bookmark;
import wad.seoul_nolgoat.domain.bookmark.BookmarkRepository;
import wad.seoul_nolgoat.domain.store.Store;
import wad.seoul_nolgoat.domain.store.StoreRepository;
import wad.seoul_nolgoat.domain.user.User;
import wad.seoul_nolgoat.domain.user.UserRepository;
import wad.seoul_nolgoat.exception.ApiException;
import wad.seoul_nolgoat.util.mapper.StoreMapper;
import wad.seoul_nolgoat.web.store.dto.response.StoreForBookmarkDto;

import java.util.List;

import static wad.seoul_nolgoat.exception.ErrorCode.*;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;

    @Transactional
    public Long save(String loginId, Long storeId) {
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new ApiException(USER_NOT_FOUND));
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new ApiException(STORE_NOT_FOUND));

        return bookmarkRepository.save(
                new Bookmark(
                        user,
                        store
                )
        ).getId();
    }

    @Transactional
    public void delete(
            String loginId,
            Long userId,
            Long storeId
    ) {
        Bookmark bookmark = bookmarkRepository.findByUserIdAndStoreId(userId, storeId)
                .orElseThrow(() -> new ApiException(BOOKMARK_NOT_FOUND));

        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new ApiException(USER_NOT_FOUND));
        if (!user.getId().equals(userId)) {
            throw new ApiException(BOOKMARK_REGISTRANT_MISMATCH);
        }

        bookmarkRepository.delete(bookmark);
    }

    public boolean checkIfBookmarked(Long userId, Long storeId) {
        return bookmarkRepository.existsByUserIdAndStoreId(userId, storeId);
    }

    public List<StoreForBookmarkDto> findBookmarkedStoresByUserId(Long userId) {
        List<Bookmark> bookmarks = bookmarkRepository.findByUserId(userId);

        return bookmarks.stream()
                .map(bookmark -> StoreMapper.toStoreForBookmarkDto(bookmark.getStore()))
                .toList();
    }
}
