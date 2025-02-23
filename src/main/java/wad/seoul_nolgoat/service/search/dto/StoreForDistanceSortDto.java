package wad.seoul_nolgoat.service.search.dto;

import org.locationtech.jts.geom.Point;
import wad.seoul_nolgoat.web.search.dto.CoordinateDto;

/**
 * @param location Querydsl 프로젝션용 파라미터
 * @param distance Querydsl 프로젝션용 파라미터
 */
public record StoreForDistanceSortDto(
        Long id,
        String name,
        CoordinateDto coordinate,
        double kakaoAverageGrade,
        double nolgoatAverageGrade,
        Point location,
        double distance
) {
}
