package wad.seoul_nolgoat.domain.store;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import wad.seoul_nolgoat.service.search.dto.StoreForDistanceSortDto;
import wad.seoul_nolgoat.service.search.dto.StoreForGradeSortDto;
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
    public List<StoreForGradeSortDto> findByRadiusRangeAndCategoryForKakaoGradeSort(
            CoordinateDto startCoordinate,
            double radiusRange,
            String category
    ) {
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
                                store.category.contains(category)
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
                                store.category.contains(category)
                        )
                        .fetch()
        );
    }

    @Override
    public List<String> findCategoriesByRadiusRange(CoordinateDto startCoordinate, double radiusRange) {
        return jpaQueryFactory.select(store.category)
                .from(store)
                .where(
                        calculateHaversineDistance(startCoordinate).loe(radiusRange),
                        store.category.isNotNull()
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
