package wad.seoul_nolgoat.domain.party;

import org.springframework.data.domain.Page;
import wad.seoul_nolgoat.web.party.request.PartySearchConditionDto;
import wad.seoul_nolgoat.web.party.response.PartyListDto;

public interface PartyRepositoryCustom {

    Page<PartyListDto> findAllWithConditionAndPagination(PartySearchConditionDto partySearchConditionDto);
}
