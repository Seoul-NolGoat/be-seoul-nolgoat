package wad.seoul_nolgoat.domain.store;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Long>, StoreRepositoryCustom {

    Page<Store> findByNameContaining(String name, Pageable pageable);

    Page<Store> findByNameContainingAndNameContaining(String name1, String name2, Pageable pageable);
}
