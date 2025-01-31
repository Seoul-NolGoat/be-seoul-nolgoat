package wad.seoul_nolgoat.domain.comment;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import wad.seoul_nolgoat.web.comment.dto.response.CommentDetailsForPartyDto;
import wad.seoul_nolgoat.web.comment.dto.response.CommentDetailsForUserDto;

import java.util.List;

import static wad.seoul_nolgoat.domain.comment.QComment.comment;

@RequiredArgsConstructor
public class CommentRepositoryCustomImpl implements CommentRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<CommentDetailsForUserDto> findCommentsByLoginId(String loginId, Pageable pageable) {
        List<CommentDetailsForUserDto> comments = jpaQueryFactory
                .select(
                        Projections.constructor(
                                CommentDetailsForUserDto.class,
                                comment.id,
                                comment.content,
                                comment.createdDate,
                                comment.isDeleted,
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

    @Override
    public List<CommentDetailsForPartyDto> findCommentsByPartyId(Long partyId) {
        return jpaQueryFactory
                .select(
                        Projections.constructor(
                                CommentDetailsForPartyDto.class,
                                comment.id,
                                Expressions.cases()
                                        .when(comment.isDeleted.isTrue())
                                        .then("")
                                        .otherwise(comment.content),
                                comment.createdDate,
                                comment.isDeleted,
                                comment.writer.id,
                                comment.writer.nickname,
                                comment.writer.profileImage
                        )
                )
                .from(comment)
                .where(comment.party.id.eq(partyId))
                .orderBy(comment.createdDate.desc())
                .fetch();
    }
}
