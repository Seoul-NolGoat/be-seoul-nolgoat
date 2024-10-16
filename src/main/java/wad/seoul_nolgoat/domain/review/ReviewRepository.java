package wad.seoul_nolgoat.domain.review;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByUserId(Long userId);

    boolean existsByUserIdAndStoreId(Long userId, Long storeId);
}
