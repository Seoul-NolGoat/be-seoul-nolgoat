package wad.seoul_nolgoat.domain.party;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import wad.seoul_nolgoat.web.party.request.PartySearchConditionDto;
import wad.seoul_nolgoat.web.party.response.HostedPartyListDto;
import wad.seoul_nolgoat.web.party.response.ParticipantDto;
import wad.seoul_nolgoat.web.party.response.PartyDetailsDto;
import wad.seoul_nolgoat.web.party.response.PartyListDto;

import java.util.List;

import static wad.seoul_nolgoat.domain.party.QParty.party;
import static wad.seoul_nolgoat.domain.party.QPartyUser.partyUser;

@RequiredArgsConstructor
public class PartyRepositoryCustomImpl implements PartyRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public PartyDetailsDto findPartyDetailsById(Long partyId) {
        return jpaQueryFactory
                .select(
                        Projections.constructor(
                                PartyDetailsDto.class,
                                party.id,
                                party.title,
                                party.content,
                                party.imageUrl,
                                party.maxCapacity,
                                party.deadline,
                                party.isClosed,
                                party.administrativeDistrict,
                                party.currentCount,
                                party.host.id,
                                party.host.nickname,
                                party.host.profileImage,
                                Projections.list(
                                        Projections.constructor(
                                                ParticipantDto.class,
                                                partyUser.participant.id,
                                                partyUser.participant.nickname,
                                                partyUser.participant.profileImage
                                        )
                                )
                        )
                )
                .from(party)
                .join(party.host)
                .leftJoin(partyUser).on(partyUser.party.eq(party)) // inner 조인을 하면 partyUser가 없을 경우, 결과가 null이 됨
                .leftJoin(partyUser.participant) // inner 조인을 하면 참여자가 없을 경우, 결과가 null이 됨
                .where(
                        party.id.eq(partyId),
                        party.isDeleted.isFalse()
                )
                .fetchOne();
    }

    @Override
    public Page<PartyListDto> findAllWithConditionAndPagination(PartySearchConditionDto partySearchConditionDto) {
        Pageable pageable = PageRequest.of(partySearchConditionDto.getPage(), partySearchConditionDto.getSize());
        String status = partySearchConditionDto.getStatus();
        String district = partySearchConditionDto.getDistrict();
        String sortField = partySearchConditionDto.getSortField();

        List<PartyListDto> parties = jpaQueryFactory
                .select(
                        Projections.constructor(
                                PartyListDto.class,
                                party.id,
                                party.title,
                                party.maxCapacity,
                                party.deadline,
                                party.isClosed,
                                party.administrativeDistrict,
                                party.currentCount,
                                party.host.id,
                                party.host.nickname,
                                party.host.profileImage
                        )
                )
                .from(party)
                .join(party.host)
                .where(
                        buildSearchCondition(status, AdministrativeDistrict.valueOf(district)),
                        party.isDeleted.isFalse()
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(getOrderSpecifier(sortField))
                .fetch();

        JPAQuery<Long> countQuery = jpaQueryFactory
                .select(party.count()) // select count(party.id)
                .from(party)
                .join(party.host)
                .where(
                        buildSearchCondition(status, AdministrativeDistrict.valueOf(district)),
                        party.isDeleted.isFalse()
                );

        return PageableExecutionUtils.getPage(parties, pageable, countQuery::fetchOne);
    }

    @Override
    public Page<HostedPartyListDto> findHostedPartiesByLoginId(String loginId, Pageable pageable) {
        List<HostedPartyListDto> parties = jpaQueryFactory
                .select(
                        Projections.constructor(
                                HostedPartyListDto.class,
                                party.id,
                                party.title,
                                party.maxCapacity,
                                party.deadline,
                                party.isClosed,
                                party.administrativeDistrict,
                                party.currentCount
                        )
                )
                .from(party)
                .where(
                        party.host.loginId.eq(loginId),
                        party.isDeleted.isFalse()
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(party.createdDate.desc())
                .fetch();

        JPAQuery<Long> countQuery = jpaQueryFactory
                .select(party.count()) // select count(party.id)
                .from(party)
                .where(
                        party.host.loginId.eq(loginId),
                        party.isDeleted.isFalse()
                );

        return PageableExecutionUtils.getPage(parties, pageable, countQuery::fetchOne);
    }

    @Override
    public Page<PartyListDto> findJoinedPartiesByLoginId(String loginId, Pageable pageable) {
        List<PartyListDto> parties = jpaQueryFactory
                .select(
                        Projections.constructor(
                                PartyListDto.class,
                                party.id,
                                party.title,
                                party.maxCapacity,
                                party.deadline,
                                party.isClosed,
                                party.administrativeDistrict,
                                party.currentCount,
                                party.host.id,
                                party.host.nickname,
                                party.host.profileImage
                        )
                )
                .from(party)
                .join(party.host)
                .join(partyUser).on(partyUser.party.eq(party))
                .join(partyUser.participant)
                .where(
                        partyUser.participant.loginId.eq(loginId),
                        party.isDeleted.isFalse()
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(party.createdDate.desc())
                .fetch();

        JPAQuery<Long> countQuery = jpaQueryFactory
                .select(party.count()) // select count(party.id)
                .from(party)
                .join(party.host)
                .join(partyUser).on(partyUser.party.eq(party))
                .join(partyUser.participant)
                .where(
                        partyUser.participant.loginId.eq(loginId),
                        party.isDeleted.isFalse()
                );

        return PageableExecutionUtils.getPage(parties, pageable, countQuery::fetchOne);
    }

    private BooleanExpression buildSearchCondition(String status, AdministrativeDistrict district) {
        if (status == null) {
            return party.administrativeDistrict.eq(district);
        }
        if (status.equals("closed")) {
            return party.isClosed.isTrue().and(party.administrativeDistrict.eq(district));
        }
        if (status.equals("opened")) {
            return party.isClosed.isFalse().and(party.administrativeDistrict.eq(district));
        }
        return null;
    }

    private OrderSpecifier<?> getOrderSpecifier(String sortField) {
        if (sortField.equals("deadline")) {
            return party.deadline.desc();
        }
        return party.createdDate.desc();
    }
}
