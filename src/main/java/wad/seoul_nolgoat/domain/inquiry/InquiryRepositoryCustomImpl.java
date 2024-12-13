package wad.seoul_nolgoat.domain.inquiry;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import wad.seoul_nolgoat.web.inquiry.dto.response.InquiryListDto;

import java.util.List;

import static wad.seoul_nolgoat.domain.inquiry.QInquiry.inquiry;

@RequiredArgsConstructor
public class InquiryRepositoryCustomImpl implements InquiryRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<InquiryListDto> findAllWithPagination(Pageable pageable) {

        List<InquiryListDto> inquiries = jpaQueryFactory
                .select(
                        Projections.constructor(
                                InquiryListDto.class,
                                inquiry.id,
                                inquiry.title,
                                inquiry.isPublic,
                                inquiry.user.id,
                                inquiry.user.nickname,
                                inquiry.user.profileImage,
                                inquiry.createdDate
                        )
                )
                .from(inquiry)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(inquiry.createdDate.desc())
                .fetch();

        JPAQuery<Long> countQuery = jpaQueryFactory
                .select(inquiry.count())
                .from(inquiry);

        return PageableExecutionUtils.getPage(inquiries, pageable, countQuery::fetchOne);
    }
}
