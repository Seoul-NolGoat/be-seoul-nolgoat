package wad.seoul_nolgoat.domain.store;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StoreRepository extends JpaRepository<Store, Long>, StoreRepositoryCustom {

    @Modifying
    @Query("UPDATE Store s SET s.nolgoatAverageGrade = :nolgoatAverageGrade WHERE s.id = :storeId")
    void updateNolgoatAverageGrade(@Param("storeId") Long storeId, @Param("nolgoatAverageGrade") double nolgoatAverageGrade);
}
