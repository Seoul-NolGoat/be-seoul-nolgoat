package wad.seoul_nolgoat.domain.party;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import wad.seoul_nolgoat.web.party.request.PartySearchConditionDto;
import wad.seoul_nolgoat.web.party.response.HostedPartyListDto;
import wad.seoul_nolgoat.web.party.response.PartyListDto;

public interface PartyRepositoryCustom {

    Page<PartyListDto> findAllWithConditionAndPagination(PartySearchConditionDto partySearchConditionDto);

    Page<HostedPartyListDto> findHostedPartiesByLoginId(String loginId, Pageable pageable);

    Page<PartyListDto> findJoinedPartiesByLoginId(String loginId, Pageable pageable);
}