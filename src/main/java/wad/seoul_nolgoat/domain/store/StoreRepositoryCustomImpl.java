package wad.seoul_nolgoat.domain.store;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import wad.seoul_nolgoat.service.search.dto.StoreForDistanceSortDto;
import wad.seoul_nolgoat.service.search.dto.StoreForGradeSortDto;
import wad.seoul_nolgoat.service.search.sort.DistanceCalculator;
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
            String category) {
        return Collections.unmodifiableList(
                jpaQueryFactory.select(
                                Projections.constructor(
                                        StoreForDistanceSortDto.class,
                                        store.id,
                                        store.name,
                                        Projections.constructor(
                                                CoordinateDto.class,
                                                store.latitude,
                                                store.longitude
                                        )
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
            String category) {
        return Collections.unmodifiableList(
                jpaQueryFactory.select(
                                Projections.constructor(
                                        StoreForGradeSortDto.class,
                                        store.id,
                                        store.name,
                                        Projections.constructor(
                                                CoordinateDto.class,
                                                store.latitude,
                                                store.longitude
                                        ),
                                        store.kakaoAverageGrade
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
            String category) {
        return Collections.unmodifiableList(
                jpaQueryFactory.select(
                                Projections.constructor(
                                        StoreForGradeSortDto.class,
                                        store.id,
                                        store.name,
                                        Projections.constructor(
                                                CoordinateDto.class,
                                                store.latitude,
                                                store.longitude
                                        ),
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

    private NumberExpression<Double> calculateHaversineDistance(CoordinateDto startCoordinate) {
        return numberTemplate(
                Double.class,
                "{0} * acos(cos(radians({1})) * cos(radians({2})) * cos(radians({3}) - radians({4})) + sin(radians({1})) * sin(radians({2})))",
                DistanceCalculator.EARTH_RADIUS_KM,
                startCoordinate.getLatitude(),
                store.latitude,
                store.longitude,
                startCoordinate.getLongitude()
        );
    }
}
