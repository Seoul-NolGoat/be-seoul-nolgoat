package wad.seoul_nolgoat.domain.bookmark;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long>, BookmarkRepositoryCustom {

    List<Bookmark> findByUserId(Long userId);

    Optional<Bookmark> findByUserIdAndStoreId(Long userId, Long storeId);

    boolean existsByUserIdAndStoreId(Long userId, Long storeId);
}
