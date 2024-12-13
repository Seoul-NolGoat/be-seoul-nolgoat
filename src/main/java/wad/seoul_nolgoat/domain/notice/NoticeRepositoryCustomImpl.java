package wad.seoul_nolgoat.domain.notice;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import wad.seoul_nolgoat.web.notice.dto.response.NoticeListDto;

import java.util.List;

import static wad.seoul_nolgoat.domain.notice.QNotice.notice;

@RequiredArgsConstructor
public class NoticeRepositoryCustomImpl implements NoticeRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<NoticeListDto> findAllWithPagination(Pageable pageable) {
        List<NoticeListDto> notices = jpaQueryFactory
                .select(
                        Projections.constructor(
                                NoticeListDto.class,
                                notice.id,
                                notice.title,
                                notice.views,
                                notice.user.id,
                                notice.user.nickname,
                                notice.user.profileImage,
                                notice.createdDate
                        )
                )
                .from(notice)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(notice.createdDate.desc())
                .fetch();

        JPAQuery<Long> countQuery = jpaQueryFactory
                .select(notice.count()) // select count(notice.id)
                .from(notice);

        return PageableExecutionUtils.getPage(notices, pageable, countQuery::fetchOne);
    }
}
