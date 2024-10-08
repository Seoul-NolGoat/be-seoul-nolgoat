package wad.seoul_nolgoat.service.bookmark;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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

import static wad.seoul_nolgoat.exception.ErrorCode.STORE_NOT_FOUND;
import static wad.seoul_nolgoat.exception.ErrorCode.USER_NOT_FOUND;

@RequiredArgsConstructor
@Service
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;

    public Long save(Long userId, Long storeId) {
        User user = userRepository.findById(userId)
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

    public void deleteById(Long userId, Long storeId) {
        if (!bookmarkRepository.existsByUserIdAndStoreId(userId, storeId)) {
            throw new RuntimeException("존재하지 않는 북마크입니다.");
        }

        bookmarkRepository.deleteByUserIdAndStoreId(userId, storeId);
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
