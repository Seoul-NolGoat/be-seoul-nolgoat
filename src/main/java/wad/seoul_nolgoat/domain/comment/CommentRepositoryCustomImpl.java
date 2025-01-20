package wad.seoul_nolgoat.domain.comment;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import wad.seoul_nolgoat.web.comment.dto.response.WrittenCommentListDto;

import java.util.List;

import static wad.seoul_nolgoat.domain.comment.QComment.comment;

@RequiredArgsConstructor
public class CommentRepositoryCustomImpl implements CommentRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<WrittenCommentListDto> findCommentsByLoginId(String loginId, Pageable pageable) {
        List<WrittenCommentListDto> comments = jpaQueryFactory
                .select(
                        Projections.constructor(
                                WrittenCommentListDto.class,
                                comment.id,
                                comment.content,
                                comment.createdDate,
                                comment.party.id
                        )
                )
                .from(comment)
                .join(comment.writer)
                .join(comment.party)
                .where(comment.writer.loginId.eq(loginId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(comment.createdDate.desc())
                .fetch();

        JPAQuery<Long> countQuery = jpaQueryFactory
                .select(comment.count())
                .from(comment)
                .join(comment.writer)
                .join(comment.party)
                .where(comment.writer.loginId.eq(loginId));

        return PageableExecutionUtils.getPage(comments, pageable, countQuery::fetchOne);
    }
}
