package wad.seoul_nolgoat.domain.store;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import wad.seoul_nolgoat.service.search.dto.StoreForDistanceSortDto;
import wad.seoul_nolgoat.service.search.dto.StoreForGradeSortDto;
import wad.seoul_nolgoat.service.search.dto.StoreForPossibleCategoriesDto;
import wad.seoul_nolgoat.web.search.dto.CoordinateDto;

import java.util.Collections;
import java.util.List;

import static com.querydsl.core.types.dsl.Expressions.numberTemplate;
import static wad.seoul_nolgoat.domain.store.QStore.store;

@RequiredArgsConstructor
public class StoreRepositoryCustomImpl implements StoreRepositoryCustom {

    private static final int MAX_RESULT_INDEX = 9;

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<StoreForDistanceSortDto> findByRadiusRangeAndCategoryForDistanceSort(
            CoordinateDto startCoordinate,
            double radiusRange,
            String category
    ) {
        return Collections.unmodifiableList(
                createBaseQueryForDistanceSorted().where(
                        buildRangeAndCategoryCondition(
                                startCoordinate,
                                radiusRange,
                                category
                        )
                ).fetch()
        );
    }

    @Override
    public List<StoreForDistanceSortDto> findByRadiusRangeAndCategoryAndStoreTypeForDistanceSort(
            CoordinateDto startCoordinate,
            double radiusRange,
            String category
    ) {
        return Collections.unmodifiableList(
                createBaseQueryForDistanceSorted().where(
                        buildRangeAndCategoryAndTypeCondition(
                                startCoordinate,
                                radiusRange,
                                category
                        )
                ).fetch()
        );
    }

    @Override
    public List<StoreForGradeSortDto> findByRadiusRangeAndCategoryForKakaoGradeSort(
            CoordinateDto startCoordinate,
            double radiusRange,
            String category
    ) {
        Long offsetValue = getOffsetForRangeAndCategory(
                startCoordinate,
                radiusRange,
                category
        );

        Double baseKakaoAverageGrade = jpaQueryFactory.select(store.kakaoAverageGrade)
                .from(store)
                .where(
                        buildRangeAndCategoryCondition(
                                startCoordinate,
                                radiusRange,
                                category
                        )
                )
                .orderBy(store.kakaoAverageGrade.desc())
                .limit(1)
                .offset(offsetValue)
                .fetchOne();

        return Collections.unmodifiableList(
                createBaseQueryForKakaoGradeSorted().where(
                        buildRangeAndCategoryCondition(
                                startCoordinate,
                                radiusRange,
                                category
                        ),
                        store.kakaoAverageGrade.goe(baseKakaoAverageGrade)
                ).fetch()
        );
    }


    @Override
    public List<StoreForGradeSortDto> findByRadiusRangeAndCategoryAndStoreTypeForKakaoGradeSort(
            CoordinateDto startCoordinate,
            double radiusRange,
            String category
    ) {
        Long offsetValue = getOffsetForRangeAndCategoryAndType(
                startCoordinate,
                radiusRange,
                category
        );

        Double baseKakaoAverageGrade = jpaQueryFactory.select(store.kakaoAverageGrade)
                .from(store)
                .where(
                        buildRangeAndCategoryAndTypeCondition(
                                startCoordinate,
                                radiusRange,
                                category
                        )
                )
                .orderBy(store.kakaoAverageGrade.desc())
                .limit(1)
                .offset(offsetValue)
                .fetchOne();

        return Collections.unmodifiableList(
                createBaseQueryForKakaoGradeSorted().where(
                        buildRangeAndCategoryAndTypeCondition(
                                startCoordinate,
                                radiusRange,
                                category
                        ),
                        store.kakaoAverageGrade.goe(baseKakaoAverageGrade)
                ).fetch()
        );
    }

    @Override
    public List<StoreForGradeSortDto> findByRadiusRangeAndCategoryForNolgoatGradeSort(
            CoordinateDto startCoordinate,
            double radiusRange,
            String category
    ) {
        Long offsetValue = getOffsetForRangeAndCategory(
                startCoordinate,
                radiusRange,
                category
        );

        Double baseNolgoatAverageGrade = jpaQueryFactory.select(store.nolgoatAverageGrade)
                .from(store)
                .where(
                        buildRangeAndCategoryCondition(
                                startCoordinate,
                                radiusRange,
                                category
                        )
                )
                .orderBy(store.nolgoatAverageGrade.desc())
                .limit(1)
                .offset(offsetValue)
                .fetchOne();

        return Collections.unmodifiableList(
                createBaseQueryForNolgoatGradeSorted().where(
                        buildRangeAndCategoryCondition(
                                startCoordinate,
                                radiusRange,
                                category
                        ),
                        store.nolgoatAverageGrade.goe(baseNolgoatAverageGrade)
                ).fetch()
        );
    }

    @Override
    public List<StoreForGradeSortDto> findByRadiusRangeAndCategoryAndStoreTypeForNolgoatGradeSort(
            CoordinateDto startCoordinate,
            double radiusRange,
            String category
    ) {
        Long offsetValue = getOffsetForRangeAndCategoryAndType(
                startCoordinate,
                radiusRange,
                category
        );

        Double baseNolgoatAverageGrade = jpaQueryFactory.select(store.nolgoatAverageGrade)
                .from(store)
                .where(
                        buildRangeAndCategoryAndTypeCondition(
                                startCoordinate,
                                radiusRange,
                                category
                        )
                )
                .orderBy(store.nolgoatAverageGrade.desc())
                .limit(1)
                .offset(offsetValue)
                .fetchOne();

        return Collections.unmodifiableList(
                createBaseQueryForNolgoatGradeSorted().where(
                        buildRangeAndCategoryAndTypeCondition(
                                startCoordinate,
                                radiusRange,
                                category
                        ),
                        store.nolgoatAverageGrade.goe(baseNolgoatAverageGrade)
                ).fetch()
        );
    }

    @Override
    public List<StoreForPossibleCategoriesDto> findCategoriesByRadiusRange(CoordinateDto startCoordinate, double radiusRange) {
        return jpaQueryFactory.select(
                        Projections.constructor(
                                StoreForPossibleCategoriesDto.class,
                                store.storeType,
                                store.category
                        )
                )
                .from(store)
                .where(
                        calculateHaversineDistance(startCoordinate).loe(radiusRange),
                        store.category.isNotNull(),
                        store.storeType.isNotNull()
                )
                .distinct()
                .fetch();
    }

    // 거리 기준 정렬을 위한 기본 쿼리
    private JPAQuery<StoreForDistanceSortDto> createBaseQueryForDistanceSorted() {
        return jpaQueryFactory.select(
                Projections.constructor(
                        StoreForDistanceSortDto.class,
                        store.id,
                        store.name,
                        Projections.constructor(
                                CoordinateDto.class,
                                numberTemplate(Double.class, "ST_Y({0})", store.location),
                                numberTemplate(Double.class, "ST_X({0})", store.location)
                        ),
                        store.kakaoAverageGrade,
                        store.nolgoatAverageGrade
                )
        ).from(store);
    }

    // 카카오 평점 기준 정렬을 위한 기본 쿼리
    private JPAQuery<StoreForGradeSortDto> createBaseQueryForKakaoGradeSorted() {
        return jpaQueryFactory.select(
                Projections.constructor(
                        StoreForGradeSortDto.class,
                        store.id,
                        store.name,
                        Projections.constructor(
                                CoordinateDto.class,
                                numberTemplate(Double.class, "ST_Y({0})", store.location),
                                numberTemplate(Double.class, "ST_X({0})", store.location)
                        ),
                        store.kakaoAverageGrade,
                        store.kakaoAverageGrade,
                        store.nolgoatAverageGrade
                )
        ).from(store);
    }

    // 놀곳 평점 기준 정렬을 위한 기본 쿼리
    private JPAQuery<StoreForGradeSortDto> createBaseQueryForNolgoatGradeSorted() {
        return jpaQueryFactory.select(
                Projections.constructor(
                        StoreForGradeSortDto.class,
                        store.id,
                        store.name,
                        Projections.constructor(
                                CoordinateDto.class,
                                numberTemplate(Double.class, "ST_Y({0})", store.location),
                                numberTemplate(Double.class, "ST_X({0})", store.location)
                        ),
                        store.nolgoatAverageGrade,
                        store.kakaoAverageGrade,
                        store.nolgoatAverageGrade
                )
        ).from(store);
    }

    // 평점 기준으로 내림차순 정렬한 뒤, 지정된 개수(10)만큼 데이터를 가져오려고 할 때, 실제 조회된 결과 개수가 기준 개수보다 적은 경우를 처리하는 로직
    // buildDistanceCategoryCondition 사용
    private Long getOffsetForRangeAndCategory(
            CoordinateDto startCoordinate,
            double radiusRange,
            String category
    ) {
        Long resultCount = jpaQueryFactory.select(store.count())
                .from(store)
                .where(
                        buildRangeAndCategoryCondition(
                                startCoordinate,
                                radiusRange,
                                category
                        )
                ).fetchOne();

        return Math.min(MAX_RESULT_INDEX, resultCount - 1);
    }

    // 평점 기준으로 내림차순 정렬한 뒤, 지정된 개수(10)만큼 데이터를 가져오려고 할 때, 실제 조회된 결과 개수가 기준 개수보다 적은 경우를 처리하는 로직
    // buildDistanceCategoryStoreTypeCondition 사용
    private Long getOffsetForRangeAndCategoryAndType(
            CoordinateDto startCoordinate,
            double radiusRange,
            String category
    ) {
        Long resultCount = jpaQueryFactory.select(store.count())
                .from(store)
                .where(
                        buildRangeAndCategoryAndTypeCondition(
                                startCoordinate,
                                radiusRange,
                                category
                        )
                ).fetchOne();

        return Math.min(MAX_RESULT_INDEX, resultCount - 1);
    }

    // 거리, 카테고리 조건 설정
    private BooleanExpression buildRangeAndCategoryCondition(
            CoordinateDto startCoordinate,
            double radiusRange,
            String category
    ) {
        return calculateHaversineDistance(startCoordinate).loe(radiusRange)
                .and(buildCategoryCondition(category));
    }

    // 거리, 카테고리, 가게 타입 조건 설정
    private BooleanExpression buildRangeAndCategoryAndTypeCondition(
            CoordinateDto startCoordinate,
            double radiusRange,
            String category
    ) {
        return calculateHaversineDistance(startCoordinate).loe(radiusRange)
                .and(buildCategoryCondition(category)
                        .or(store.storeType.eq(StoreType.getStoreTypeByName(category))));
    }

    // 해당 Enum(category)에 속한 모든 카테고리들을 조건에 추가
    private BooleanExpression buildCategoryCondition(String category) {
        return StoreCategory.findRelatedCategoryNames(category).stream()
                .map(store.category::contains)
                .reduce(BooleanExpression::or)
                .get(); // 비어있는 경우는 없음
    }

    private NumberExpression<Double> calculateHaversineDistance(CoordinateDto startCoordinate) {
        return numberTemplate(
                Double.class,
                "ST_Distance_Sphere(Point({0}, {1}), Point(ST_X({2}), ST_Y({2})))",
                startCoordinate.getLongitude(),
                startCoordinate.getLatitude(),
                store.location
        ).divide(1000.0); // km로 변환
    }
}
