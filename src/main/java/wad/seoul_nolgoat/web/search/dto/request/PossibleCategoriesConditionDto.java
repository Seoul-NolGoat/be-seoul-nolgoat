package wad.seoul_nolgoat.web.search.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import wad.seoul_nolgoat.web.search.dto.CoordinateDto;

@Getter
@RequiredArgsConstructor
public class PossibleCategoriesConditionDto {

    private final CoordinateDto startCoordinate;
    private final double radiusRange;
}
