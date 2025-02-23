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
import wad.seoul_nolgoat.util.mapper.StoreMapper;
import wad.seoul_nolgoat.web.search.dto.CoordinateDto;
import wad.seoul_nolgoat.web.store.dto.response.StoreDetailsDto;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.querydsl.core.types.dsl.Expressions.numberTemplate;
import static wad.seoul_nolgoat.domain.review.QReview.review;
import static wad.seoul_nolgoat.domain.store.QStore.store;
import static wad.seoul_nolgoat.domain.user.QUser.user;

@RequiredArgsConstructor
public class StoreRepositoryCustomImpl implements StoreRepositoryCustom {

    private static final String ST_Y_TEMPLATE = "ST_Y({0})";  // Y 좌표용 템플릿
    private static final String ST_X_TEMPLATE = "ST_X({0})";  // X 좌표용 템플릿
    private static final int GRADE_SORT_MAX_RESULT_COUNT = 10; // 평점 기준 정렬 시, 최대 개수 기준
    private static final int DISTANCE_SORT_MAX_RESULT_COUNT = 30; // 거리 기준 정렬 시, 최대 개수 기준

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<StoreDetailsDto> findStoreWithReviewsByStoreId(Long storeId) {
        List<Store> result = jpaQueryFactory
                .selectFrom(store)
                .leftJoin(store.reviews, review).fetchJoin()
                .leftJoin(review.user, user).fetchJoin()
                .where(store.id.eq(storeId))
                .orderBy(review.createdDate.desc())
                .fetch();

        // 해당 id의 store가 존재하지 않으면 빈 Optional 반환
        if (result.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(StoreMapper.toStoreDetailsDto(
                        result.get(0), // 단건 조회이기 때문에 가게 정보는 모든 튜플에서 동일
                        Collections.unmodifiableList(result.get(0).getReviews())
                )
        );
    }

    @Override
    public List<StoreForDistanceSortDto> findByRadiusRangeAndCategoryForDistanceSort(
            CoordinateDto startCoordinate,
            double radiusRange,
            String category
    ) {
        NumberExpression<Double> distance = calculateHaversineDistance(startCoordinate);
        List<StoreForDistanceSortDto> result = createBaseQueryForDistanceSorted(distance)
                .where(
                        buildRangeAndCategoryCondition(
                                startCoordinate,
                                radiusRange,
                                category
                        )
                )
                .orderBy(distance.asc())
                .fetch();

        // 조회 결과를 모두 사용하면 개수가 너무 많기 때문에 적당한 기준 설정
        int resultCount = Math.min(DISTANCE_SORT_MAX_RESULT_COUNT, result.size());
        double baseDistance = result.get(resultCount - 1).distance();

        return result.stream()
                .filter(store -> store.distance() <= baseDistance)
                .toList();
    }

    @Override
    public List<StoreForDistanceSortDto> findByRadiusRangeAndCategoryAndStoreTypeForDistanceSort(
            CoordinateDto startCoordinate,
            double radiusRange,
            String category
    ) {
        NumberExpression<Double> distance = calculateHaversineDistance(startCoordinate);
        List<StoreForDistanceSortDto> result = createBaseQueryForDistanceSorted(distance)
                .where(
                        buildRangeAndCategoryAndTypeCondition(
                                startCoordinate,
                                radiusRange,
                                category
                        )
                )
                .orderBy(distance.asc())
                .fetch();

        // 조회 결과를 모두 사용하면 개수가 너무 많기 때문에 적당한 기준 설정
        int resultCount = Math.min(DISTANCE_SORT_MAX_RESULT_COUNT, result.size());
        double baseDistance = result.get(resultCount - 1).distance();

        return result.stream()
                .filter(store -> store.distance() <= baseDistance)
                .toList();
    }

    @Override
    public List<StoreForGradeSortDto> findByRadiusRangeAndCategoryForKakaoGradeSort(
            CoordinateDto startCoordinate,
            double radiusRange,
            String category
    ) {
        List<Double> baseGrades = jpaQueryFactory
                .select(store.kakaoAverageGrade)
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
                .offset(GRADE_SORT_MAX_RESULT_COUNT - 1)
                .fetch();
        Double baseGrade = baseGrades.get(0);

        return createBaseQueryForKakaoGradeSorted()
                .where(
                        buildRangeAndCategoryCondition(
                                startCoordinate,
                                radiusRange,
                                category
                        ),
                        store.kakaoAverageGrade.goe(baseGrade)
                )
                .fetch();
    }

    @Override
    public List<StoreForGradeSortDto> findByRadiusRangeAndCategoryAndStoreTypeForKakaoGradeSort(
            CoordinateDto startCoordinate,
            double radiusRange,
            String category
    ) {
        List<Double> baseGrades = jpaQueryFactory
                .select(store.kakaoAverageGrade)
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
                .offset(GRADE_SORT_MAX_RESULT_COUNT - 1)
                .fetch();
        Double baseGrade = baseGrades.get(0);

        return createBaseQueryForKakaoGradeSorted()
                .where(
                        buildRangeAndCategoryAndTypeCondition(
                                startCoordinate,
                                radiusRange,
                                category
                        ),
                        store.kakaoAverageGrade.goe(baseGrade)
                )
                .fetch();
    }

    @Override
    public List<StoreForGradeSortDto> findByRadiusRangeAndCategoryForNolgoatGradeSort(
            CoordinateDto startCoordinate,
            double radiusRange,
            String category
    ) {
        List<Double> baseGrades = jpaQueryFactory
                .select(store.nolgoatAverageGrade)
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
                .offset(GRADE_SORT_MAX_RESULT_COUNT - 1)
                .fetch();
        Double baseGrade = baseGrades.get(0);

        return createBaseQueryForNolgoatGradeSorted()
                .where(
                        buildRangeAndCategoryCondition(
                                startCoordinate,
                                radiusRange,
                                category
                        ),
                        store.nolgoatAverageGrade.goe(baseGrade)
                )
                .fetch();
    }

    @Override
    public List<StoreForGradeSortDto> findByRadiusRangeAndCategoryAndStoreTypeForNolgoatGradeSort(
            CoordinateDto startCoordinate,
            double radiusRange,
            String category
    ) {
        List<Double> baseGrades = jpaQueryFactory
                .select(store.nolgoatAverageGrade)
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
                .offset(GRADE_SORT_MAX_RESULT_COUNT - 1)
                .fetch();
        Double baseGrade = baseGrades.get(0);

        return createBaseQueryForNolgoatGradeSorted()
                .where(
                        buildRangeAndCategoryAndTypeCondition(
                                startCoordinate,
                                radiusRange,
                                category
                        ),
                        store.nolgoatAverageGrade.goe(baseGrade)
                )
                .fetch();
    }

    @Override
    public List<StoreForPossibleCategoriesDto> findCategoriesByRadiusRange(CoordinateDto startCoordinate, double radiusRange) {
        return jpaQueryFactory
                .select(
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
                .fetch();
    }

    // 거리 기준 정렬을 위한 기본 쿼리
    private JPAQuery<StoreForDistanceSortDto> createBaseQueryForDistanceSorted(NumberExpression<Double> distance) {
        return jpaQueryFactory
                .select(
                        Projections.constructor(
                                StoreForDistanceSortDto.class,
                                store.id,
                                store.name,
                                Projections.constructor(
                                        CoordinateDto.class,
                                        numberTemplate(Double.class, ST_Y_TEMPLATE, store.location),
                                        numberTemplate(Double.class, ST_X_TEMPLATE, store.location)
                                ),
                                store.kakaoAverageGrade,
                                store.nolgoatAverageGrade,
                                store.location,
                                distance.as("distance")
                        )
                )
                .from(store);
    }

    // 카카오 평점 기준 정렬을 위한 기본 쿼리
    private JPAQuery<StoreForGradeSortDto> createBaseQueryForKakaoGradeSorted() {
        return jpaQueryFactory
                .select(
                        Projections.constructor(
                                StoreForGradeSortDto.class,
                                store.id,
                                store.name,
                                Projections.constructor(
                                        CoordinateDto.class,
                                        numberTemplate(Double.class, ST_Y_TEMPLATE, store.location),
                                        numberTemplate(Double.class, ST_X_TEMPLATE, store.location)
                                ),
                                store.kakaoAverageGrade,
                                store.kakaoAverageGrade,
                                store.nolgoatAverageGrade
                        )
                )
                .from(store);
    }

    // 놀곳 평점 기준 정렬을 위한 기본 쿼리
    private JPAQuery<StoreForGradeSortDto> createBaseQueryForNolgoatGradeSorted() {
        return jpaQueryFactory
                .select(
                        Projections.constructor(
                                StoreForGradeSortDto.class,
                                store.id,
                                store.name,
                                Projections.constructor(
                                        CoordinateDto.class,
                                        numberTemplate(Double.class, ST_Y_TEMPLATE, store.location),
                                        numberTemplate(Double.class, ST_X_TEMPLATE, store.location)
                                ),
                                store.nolgoatAverageGrade,
                                store.kakaoAverageGrade,
                                store.nolgoatAverageGrade
                        )
                )
                .from(store);
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
