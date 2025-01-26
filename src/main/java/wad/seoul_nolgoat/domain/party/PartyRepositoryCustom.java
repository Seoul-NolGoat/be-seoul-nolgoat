package wad.seoul_nolgoat.domain.party;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import wad.seoul_nolgoat.web.party.request.PartySearchConditionDto;
import wad.seoul_nolgoat.web.party.response.PartyDetailsDto;

public interface PartyRepositoryCustom {

    PartyDetailsDto findPartyDetailsById(Long partyId);

    Page<Party> findAllWithConditionAndPagination(PartySearchConditionDto partySearchConditionDto);

    Page<Party> findHostedPartiesByLoginId(String loginId, Pageable pageable);

    Page<Party> findJoinedPartiesByLoginId(String loginId, Pageable pageable);
}