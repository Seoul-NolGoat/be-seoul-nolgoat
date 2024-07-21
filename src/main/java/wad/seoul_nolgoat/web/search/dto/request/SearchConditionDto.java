package wad.seoul_nolgoat.web.search.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import wad.seoul_nolgoat.web.search.dto.CoordinateDto;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class SearchConditionDto {

    private final CoordinateDto startCoordinate;
    private final String criteria;
    private final List<String> categories;
}
