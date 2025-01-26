package wad.seoul_nolgoat.domain.partyuser;

import wad.seoul_nolgoat.web.party.response.ParticipantDto;

import java.util.List;

public interface PartyUserRepositoryCustom {

    List<ParticipantDto> findParticipantsByPartyId(Long partyId);
}
