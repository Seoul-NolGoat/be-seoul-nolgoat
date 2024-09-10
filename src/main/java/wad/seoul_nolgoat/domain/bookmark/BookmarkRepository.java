package wad.seoul_nolgoat.domain.bookmark;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    boolean existsByUserIdAndStoreId(Long userId, Long storeId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Bookmark b WHERE b.user.id = :userId AND b.store.id = :storeId")
    void deleteByUserIdAndStoreId(@Param("userId") Long userId, @Param("storeId") Long storeId);
}
