package wad.seoul_nolgoat.web.store.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import wad.seoul_nolgoat.web.search.dto.CoordinateDto;

@Getter
@RequiredArgsConstructor
public class StoreForCombinationDto {

    private final Long id;
    private final String name;
    private final CoordinateDto coordinate;
    private final double kakaoAverageGrade;
    private final double nolgoatAverageGrade;
}
