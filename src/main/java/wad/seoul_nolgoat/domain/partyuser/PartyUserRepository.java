package wad.seoul_nolgoat.domain.partyuser;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PartyUserRepository extends JpaRepository<PartyUser, Long>, PartyUserRepositoryCustom {

    boolean existsByPartyIdAndParticipantId(Long partyId, Long participantId);

    Optional<PartyUser> findByPartyIdAndParticipantId(Long partyId, Long participantId);
}
