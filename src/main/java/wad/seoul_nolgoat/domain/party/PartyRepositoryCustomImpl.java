package wad.seoul_nolgoat.domain.party;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import wad.seoul_nolgoat.web.party.request.PartySearchConditionDto;

import java.util.List;
import java.util.Optional;

import static wad.seoul_nolgoat.domain.party.QParty.party;
import static wad.seoul_nolgoat.domain.partyuser.QPartyUser.partyUser;

@RequiredArgsConstructor
public class PartyRepositoryCustomImpl implements PartyRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<Party> findPartyByIdWithFetchJoin(Long partyId) {
        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(party)
                .join(party.host).fetchJoin()
                .where(
                        party.id.eq(partyId),
                        party.isDeleted.isFalse()
                )
                .fetchOne());
    }

    @Override
    public Page<Party> findAllWithConditionAndPagination(PartySearchConditionDto partySearchConditionDto) {
        Pageable pageable = PageRequest.of(partySearchConditionDto.getPage(), partySearchConditionDto.getSize());
        PartyStatus status = PartyStatus.fromString(partySearchConditionDto.getStatus());
        AdministrativeDistrict district = AdministrativeDistrict.fromString(partySearchConditionDto.getDistrict());
        String sortField = partySearchConditionDto.getSortField();

        List<Party> parties = jpaQueryFactory
                .selectFrom(party)
                .join(party.host).fetchJoin()
                .where(
                        buildSearchCondition(status, district),
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
                        buildSearchCondition(status, district),
                        party.isDeleted.isFalse()
                );

        return PageableExecutionUtils.getPage(parties, pageable, countQuery::fetchOne);
    }

    @Override
    public Page<Party> findHostedPartiesByLoginId(String loginId, Pageable pageable) {
        List<Party> parties = jpaQueryFactory
                .selectFrom(party)
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
    public Page<Party> findJoinedPartiesByLoginId(String loginId, Pageable pageable) {
        List<Party> parties = jpaQueryFactory
                .selectFrom(party)
                .join(party.host).fetchJoin()
                .leftJoin(partyUser).on(partyUser.party.eq(party))
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
                .leftJoin(partyUser).on(partyUser.party.eq(party))
                .join(partyUser.participant)
                .where(
                        partyUser.participant.loginId.eq(loginId),
                        party.isDeleted.isFalse()
                );

        return PageableExecutionUtils.getPage(parties, pageable, countQuery::fetchOne);
    }

    private BooleanExpression buildSearchCondition(PartyStatus status, AdministrativeDistrict district) {
        BooleanExpression statusCondition = createStatusCondition(status);
        BooleanExpression districtCondition = createDistrictCondition(district);

        if (statusCondition == null && districtCondition == null) {
            return null;
        }
        if (statusCondition == null) {
            return districtCondition;
        }
        if (districtCondition == null) {
            return statusCondition;
        }

        return statusCondition.and(districtCondition);
    }

    private BooleanExpression createStatusCondition(PartyStatus status) {
        if (status == null) {
            return null;
        }
        if (status.equals(PartyStatus.OPENED)) {
            return party.isClosed.isFalse();
        }
        if (status.equals(PartyStatus.CLOSED)) {
            return party.isClosed.isTrue();
        }
        return null;
    }

    private BooleanExpression createDistrictCondition(AdministrativeDistrict district) {
        if (district == null) {
            return null;
        }
        return party.administrativeDistrict.eq(district);
    }

    private OrderSpecifier<?> getOrderSpecifier(String sortField) {
        if (sortField.equals("meetingDate")) {
            return party.meetingDate.asc();
        }
        return party.createdDate.desc();
    }
}
