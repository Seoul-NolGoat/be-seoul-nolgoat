package wad.seoul_nolgoat.domain.party;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import wad.seoul_nolgoat.web.party.request.PartySearchConditionDto;
import wad.seoul_nolgoat.web.party.response.PartyDetailsDto;
import wad.seoul_nolgoat.web.party.response.PartyDetailsForListDto;
import wad.seoul_nolgoat.web.party.response.PartyDetailsForUserDto;

public interface PartyRepositoryCustom {

    PartyDetailsDto findPartyDetailsById(Long partyId);

    Page<Party> findAllWithConditionAndPagination(PartySearchConditionDto partySearchConditionDto);

    Page<PartyDetailsForUserDto> findHostedPartiesByLoginId(String loginId, Pageable pageable);

    Page<PartyDetailsForListDto> findJoinedPartiesByLoginId(String loginId, Pageable pageable);
}