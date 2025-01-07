package wad.seoul_nolgoat.domain.review;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import wad.seoul_nolgoat.web.review.dto.response.ReviewDetailsForUserDto;

import java.util.List;

import static wad.seoul_nolgoat.domain.review.QReview.review;

@RequiredArgsConstructor
public class ReviewRepositoryCustomImpl implements ReviewRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<ReviewDetailsForUserDto> findReviewDetailsByLoginId(String loginId, Pageable pageable) {
        List<ReviewDetailsForUserDto> reviews = jpaQueryFactory
                .select(
                        Projections.constructor(
                                ReviewDetailsForUserDto.class,
                                review.id,
                                review.grade,
                                review.content,
                                review.imageUrl,
                                review.store.id,
                                review.store.name
                        )
                )
                .from(review)
                .join(review.store)
                .join(review.user)
                .where(review.user.loginId.eq(loginId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(review.createdDate.desc())
                .fetch();

        JPAQuery<Long> countQuery = jpaQueryFactory
                .select(review.count()) // select count(review.id)
                .from(review)
                .join(review.store)
                .join(review.user)
                .where(review.user.loginId.eq(loginId));

        return PageableExecutionUtils.getPage(reviews, pageable, countQuery::fetchOne);
    }
}
