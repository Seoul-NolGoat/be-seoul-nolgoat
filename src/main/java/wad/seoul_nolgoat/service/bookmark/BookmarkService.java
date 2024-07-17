package wad.seoul_nolgoat.service.bookmark;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import wad.seoul_nolgoat.domain.bookmark.Bookmark;
import wad.seoul_nolgoat.domain.bookmark.BookmarkRepository;
import wad.seoul_nolgoat.domain.store.Store;
import wad.seoul_nolgoat.domain.store.StoreRepository;
import wad.seoul_nolgoat.domain.user.User;
import wad.seoul_nolgoat.domain.user.UserRepository;

@RequiredArgsConstructor
@Service
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;

    public Long save(Long userId, Long storeId) {
        User user = userRepository.findById(userId).get();
        Store store = storeRepository.findById(storeId).get();

        return bookmarkRepository.save(
                new Bookmark(
                        user,
                        store
                )
        ).getId();
    }

    public void deleteById(Long bookmarkId) {
        bookmarkRepository.deleteById(bookmarkId);
    }
}
