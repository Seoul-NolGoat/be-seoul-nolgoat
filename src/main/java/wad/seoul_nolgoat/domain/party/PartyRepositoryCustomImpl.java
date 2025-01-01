package wad.seoul_nolgoat.domain.party;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import wad.seoul_nolgoat.web.party.request.PartySearchConditionDto;
import wad.seoul_nolgoat.web.party.response.PartyListDto;

import java.util.List;

import static wad.seoul_nolgoat.domain.party.QParty.party;
import static wad.seoul_nolgoat.domain.party.QPartyUser.partyUser;

@RequiredArgsConstructor
public class PartyRepositoryCustomImpl implements PartyRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

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
                                JPAExpressions
                                        .select(partyUser.count())
                                        .from(partyUser)
                                        .where(partyUser.party.id.eq(party.id)),
                                party.host.id,
                                party.host.nickname,
                                party.host.profileImage
                        )
                )
                .from(party)
                .where(buildSearchCondition(status, AdministrativeDistrict.valueOf(district)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(getOrderSpecifier(sortField))
                .fetch();

        JPAQuery<Long> countQuery = jpaQueryFactory
                .select(party.count()) // select count(party.id)
                .from(party)
                .where(buildSearchCondition(status, AdministrativeDistrict.valueOf(district)));

        return PageableExecutionUtils.getPage(parties, pageable, countQuery::fetchOne);
    }

    private BooleanExpression buildSearchCondition(String status, AdministrativeDistrict district) {
        if (status.equals("closed")) {
            return party.isClosed.isTrue().and(party.administrativeDistrict.eq(district));
        }
        if (status.equals("opened")) {
            return party.isClosed.isFalse().and(party.administrativeDistrict.eq(district));
        }
        return party.administrativeDistrict.eq(district);
    }

    private OrderSpecifier<?> getOrderSpecifier(String sortField) {
        if (sortField.equals("deadline")) {
            return party.deadline.desc();
        }
        return party.createdDate.desc();
    }
}
