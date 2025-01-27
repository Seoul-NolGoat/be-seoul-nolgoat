package wad.seoul_nolgoat.domain.partyuser;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import wad.seoul_nolgoat.web.party.response.ParticipantDto;

import java.util.List;

import static wad.seoul_nolgoat.domain.partyuser.QPartyUser.partyUser;

@RequiredArgsConstructor
public class PartyUserRepositoryCustomImpl implements PartyUserRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<ParticipantDto> findParticipantsByPartyId(Long partyId) {
        jpaQueryFactory
                .select(
                        Projections.constructor(
                                ParticipantDto.class,
                                partyUser.participant.id,
                                partyUser.participant.nickname,
                                partyUser.participant.profileImage,
                                partyUser.participant.loginId
                        )
                )
                .from(partyUser)
                .where(partyUser.party.id.eq(partyId))
                .orderBy(partyUser.createdDate.asc())
                .fetch();

        return List.of();
    }
}
