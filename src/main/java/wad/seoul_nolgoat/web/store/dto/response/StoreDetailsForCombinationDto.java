package wad.seoul_nolgoat.web.store.dto.response;

import wad.seoul_nolgoat.web.search.dto.CoordinateDto;

public record StoreDetailsForCombinationDto(
        Long storeId,
        String name,
        CoordinateDto coordinate,
        double kakaoAverageGrade,
        double nolgoatAverageGrade
) {
}