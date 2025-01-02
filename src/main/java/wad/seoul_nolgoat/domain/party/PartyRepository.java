package wad.seoul_nolgoat.domain.party;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PartyRepository extends JpaRepository<Party, Long>, PartyRepositoryCustom {

    @Query("SELECT p FROM Party p " +
            "LEFT JOIN FETCH p.host " +
            "WHERE p.id = :partyId")
    Optional<Party> findByIdWithFetchJoin(Long partyId);
}
