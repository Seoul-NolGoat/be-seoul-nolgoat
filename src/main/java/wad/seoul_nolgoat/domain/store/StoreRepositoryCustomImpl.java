package wad.seoul_nolgoat.domain.store;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.NumberExpression;
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

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<StoreForDistanceSortDto> findByRadiusRangeAndCategoryForDistanceSort(
            CoordinateDto startCoordinate,
            double radiusRange,
            String category
    ) {
        return Collections.unmodifiableList(
                jpaQueryFactory.select(
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
                        )
                        .from(store)
                        .where(
                                calculateHaversineDistance(startCoordinate).loe(radiusRange),
                                store.category.contains(category)
                        )
                        .fetch()
        );
    }

    @Override
    public List<StoreForDistanceSortDto> findByRadiusRangeAndStoreTypeForDistanceSort(
            CoordinateDto startCoordinate,
            double radiusRange,
            StoreType storeType
    ) {
        return Collections.unmodifiableList(
                jpaQueryFactory.select(
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
                        )
                        .from(store)
                        .where(
                                calculateHaversineDistance(startCoordinate).loe(radiusRange),
                                store.storeType.eq(storeType)
                        )
                        .fetch()
        );
    }

    @Override
    public List<StoreForGradeSortDto> findByRadiusRangeAndCategoryForKakaoGradeSort(
            CoordinateDto startCoordinate,
            double radiusRange,
            String category
    ) {
        Double baseKakaoAverageGrade = jpaQueryFactory.select(store.kakaoAverageGrade)
                .from(store)
                .where(
                        calculateHaversineDistance(startCoordinate).loe(radiusRange),
                        store.category.contains(category)
                )
                .orderBy(store.kakaoAverageGrade.desc())
                .limit(1)
                .offset(9)
                .fetchOne();

        return Collections.unmodifiableList(
                jpaQueryFactory.select(
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
                        )
                        .from(store)
                        .where(
                                calculateHaversineDistance(startCoordinate).loe(radiusRange),
                                store.category.contains(category),
                                store.kakaoAverageGrade.goe(baseKakaoAverageGrade)
                        )
                        .fetch()
        );
    }

    @Override
    public List<StoreForGradeSortDto> findByRadiusRangeAndStoreTypeForKakaoGradeSort(
            CoordinateDto startCoordinate,
            double radiusRange,
            StoreType storeType
    ) {
        Double baseKakaoAverageGrade = jpaQueryFactory.select(store.kakaoAverageGrade)
                .from(store)
                .where(
                        calculateHaversineDistance(startCoordinate).loe(radiusRange),
                        store.storeType.eq(storeType)
                )
                .orderBy(store.kakaoAverageGrade.desc())
                .limit(1)
                .offset(9)
                .fetchOne();

        return Collections.unmodifiableList(
                jpaQueryFactory.select(
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
                        )
                        .from(store)
                        .where(
                                calculateHaversineDistance(startCoordinate).loe(radiusRange),
                                store.storeType.eq(storeType),
                                store.kakaoAverageGrade.goe(baseKakaoAverageGrade)
                        )
                        .fetch()
        );
    }

    @Override
    public List<StoreForGradeSortDto> findByRadiusRangeAndCategoryForNolgoatGradeSort(
            CoordinateDto startCoordinate,
            double radiusRange,
            String category
    ) {
        Double baseNolgoatAverageGrade = jpaQueryFactory.select(store.nolgoatAverageGrade)
                .from(store)
                .where(
                        calculateHaversineDistance(startCoordinate).loe(radiusRange),
                        store.category.contains(category)
                )
                .orderBy(store.nolgoatAverageGrade.desc())
                .limit(1)
                .offset(9)
                .fetchOne();

        return Collections.unmodifiableList(
                jpaQueryFactory.select(
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
                        )
                        .from(store)
                        .where(
                                calculateHaversineDistance(startCoordinate).loe(radiusRange),
                                store.category.contains(category),
                                store.nolgoatAverageGrade.goe(baseNolgoatAverageGrade)
                        )
                        .fetch()
        );
    }

    @Override
    public List<StoreForGradeSortDto> findByRadiusRangeAndStoreTypeForNolgoatGradeSort(
            CoordinateDto startCoordinate,
            double radiusRange,
            StoreType storeType
    ) {
        Double baseNolgoatAverageGrade = jpaQueryFactory.select(store.nolgoatAverageGrade)
                .from(store)
                .where(
                        calculateHaversineDistance(startCoordinate).loe(radiusRange),
                        store.storeType.eq(storeType)
                )
                .orderBy(store.nolgoatAverageGrade.desc())
                .limit(1)
                .offset(9)
                .fetchOne();

        return Collections.unmodifiableList(
                jpaQueryFactory.select(
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
                        )
                        .from(store)
                        .where(
                                calculateHaversineDistance(startCoordinate).loe(radiusRange),
                                store.storeType.eq(storeType),
                                store.nolgoatAverageGrade.goe(baseNolgoatAverageGrade)
                        )
                        .fetch()
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
