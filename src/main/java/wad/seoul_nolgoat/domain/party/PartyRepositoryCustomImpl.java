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
import wad.seoul_nolgoat.web.comment.dto.response.CommentListForPartyDto;
import wad.seoul_nolgoat.web.party.request.PartySearchConditionDto;
import wad.seoul_nolgoat.web.party.response.HostedPartyListDto;
import wad.seoul_nolgoat.web.party.response.ParticipantDto;
import wad.seoul_nolgoat.web.party.response.PartyDetailsDto;
import wad.seoul_nolgoat.web.party.response.PartyListDto;

import java.util.List;

import static wad.seoul_nolgoat.domain.comment.QComment.comment;
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
                                party.maxCapacity,
                                party.meetingDate,
                                party.isClosed,
                                party.administrativeDistrict,
                                party.currentCount,
                                party.createdDate,
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
                                ),
                                Projections.list(
                                        Projections.constructor(
                                                CommentListForPartyDto.class,
                                                comment.id,
                                                comment.content,
                                                comment.createdDate,
                                                comment.party.id,
                                                comment.writer.id,
                                                comment.writer.nickname,
                                                comment.writer.profileImage
                                        )
                                )
                        )
                )
                .from(party)
                .join(party.host)
                .leftJoin(partyUser).on(partyUser.party.eq(party)) // inner 조인을 하면 partyUser가 없을 경우, 결과가 null이 됨
                .leftJoin(partyUser.participant) // inner 조인을 하면 참여자가 없을 경우, 결과가 null이 됨
                .leftJoin(comment).on(comment.party.eq(party))
                .leftJoin(comment.writer)
                .where(
                        party.id.eq(partyId),
                        party.isDeleted.isFalse()
                )
                .fetchOne();
    }

    @Override
    public Page<PartyListDto> findAllWithConditionAndPagination(PartySearchConditionDto partySearchConditionDto) {
        Pageable pageable = PageRequest.of(partySearchConditionDto.getPage(), partySearchConditionDto.getSize());
        PartyStatus status = PartyStatus.fromString(partySearchConditionDto.getStatus());
        AdministrativeDistrict district = AdministrativeDistrict.fromString(partySearchConditionDto.getDistrict());
        String sortField = partySearchConditionDto.getSortField();

        List<PartyListDto> parties = jpaQueryFactory
                .select(
                        Projections.constructor(
                                PartyListDto.class,
                                party.id,
                                party.title,
                                party.maxCapacity,
                                party.meetingDate,
                                party.isClosed,
                                party.administrativeDistrict,
                                party.currentCount,
                                party.createdDate,
                                party.host.id,
                                party.host.nickname,
                                party.host.profileImage
                        )
                )
                .from(party)
                .join(party.host)
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
    public Page<HostedPartyListDto> findHostedPartiesByLoginId(String loginId, Pageable pageable) {
        List<HostedPartyListDto> parties = jpaQueryFactory
                .select(
                        Projections.constructor(
                                HostedPartyListDto.class,
                                party.id,
                                party.title,
                                party.maxCapacity,
                                party.meetingDate,
                                party.isClosed,
                                party.administrativeDistrict,
                                party.currentCount,
                                party.createdDate
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
                                party.meetingDate,
                                party.isClosed,
                                party.administrativeDistrict,
                                party.currentCount,
                                party.createdDate,
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
            return party.meetingDate.desc();
        }
        return party.createdDate.desc();
    }
}
