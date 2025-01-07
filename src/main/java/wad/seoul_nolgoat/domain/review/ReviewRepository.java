package wad.seoul_nolgoat.domain.review;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewRepositoryCustom {

    boolean existsByUserIdAndStoreId(Long userId, Long storeId);
}
