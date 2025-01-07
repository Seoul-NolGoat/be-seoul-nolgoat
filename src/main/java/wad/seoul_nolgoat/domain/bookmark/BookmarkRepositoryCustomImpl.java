package wad.seoul_nolgoat.domain.bookmark;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import wad.seoul_nolgoat.web.bookmark.dto.response.StoreForBookmarkDto;

import java.util.List;

import static wad.seoul_nolgoat.domain.bookmark.QBookmark.bookmark;

@RequiredArgsConstructor
public class BookmarkRepositoryCustomImpl implements BookmarkRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<StoreForBookmarkDto> findBookmarkedStoresByLoginId(String loginId, Pageable pageable) {
        List<StoreForBookmarkDto> bookmarks = jpaQueryFactory
                .select(
                        Projections.constructor(
                                StoreForBookmarkDto.class,
                                bookmark.id,
                                bookmark.store.id,
                                bookmark.store.storeType,
                                bookmark.store.name,
                                bookmark.store.lotAddress,
                                bookmark.store.roadAddress,
                                bookmark.store.kakaoAverageGrade,
                                bookmark.store.nolgoatAverageGrade
                        )
                )
                .from(bookmark)
                .join(bookmark.store)
                .join(bookmark.user)
                .where(bookmark.user.loginId.eq(loginId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(bookmark.createdDate.desc())
                .fetch();

        JPAQuery<Long> countQuery = jpaQueryFactory
                .select(bookmark.count()) // select count(bookmark.id)
                .from(bookmark)
                .join(bookmark.store)
                .join(bookmark.user)
                .where(bookmark.user.loginId.eq(loginId));

        return PageableExecutionUtils.getPage(bookmarks, pageable, countQuery::fetchOne);
    }
}
