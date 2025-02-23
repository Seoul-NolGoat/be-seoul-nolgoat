package wad.seoul_nolgoat.service.search.dto;

import wad.seoul_nolgoat.web.search.dto.CoordinateDto;

public record StoreForGradeSortDto(
        Long id,
        String name,
        CoordinateDto coordinate,
        double averageGrade,
        double kakaoAverageGrade,
        double nolgoatAverageGrade
) {
}
