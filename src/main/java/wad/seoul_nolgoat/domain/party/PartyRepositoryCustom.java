package wad.seoul_nolgoat.domain.party;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import wad.seoul_nolgoat.web.party.request.PartySearchConditionDto;

import java.util.Optional;

public interface PartyRepositoryCustom {

    Optional<Party> findPartyByIdWithFetchJoin(Long partyId);

    Page<Party> findAllWithConditionAndPagination(PartySearchConditionDto partySearchConditionDto);

    Page<Party> findHostedPartiesByLoginId(String loginId, Pageable pageable);

    Page<Party> findJoinedPartiesByLoginId(String loginId, Pageable pageable);
}