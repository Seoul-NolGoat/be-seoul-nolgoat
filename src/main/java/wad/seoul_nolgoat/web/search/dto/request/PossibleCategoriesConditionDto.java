package wad.seoul_nolgoat.web.search.dto.request;

import wad.seoul_nolgoat.web.search.dto.CoordinateDto;

public record PossibleCategoriesConditionDto(CoordinateDto startCoordinate, double radiusRange) {
}
