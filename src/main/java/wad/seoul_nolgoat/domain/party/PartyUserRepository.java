package wad.seoul_nolgoat.domain.party;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PartyUserRepository extends JpaRepository<PartyUser, Long> {

    @Query("SELECT COUNT(pu) FROM PartyUser pu WHERE pu.party.id = :partyId")
    int countByPartyId(Long partyId);

    boolean existsByPartyIdAndParticipantId(Long partyId, Long participantId);

    Optional<PartyUser> findByPartyIdAndParticipantId(Long partyId, Long participantId);
}
