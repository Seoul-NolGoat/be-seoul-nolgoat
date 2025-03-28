package wad.seoul_nolgoat.web.search.dto.request;

import lombok.Getter;
import wad.seoul_nolgoat.web.search.dto.CoordinateDto;

import java.util.Arrays;
import java.util.List;

@Getter
public class SearchConditionDto {

    private static final String SPLIT_DELIMITER = ",";

    private final CoordinateDto startCoordinate;
    private final double radiusRange;
    private final String criteria;
    private final List<String> categories;

    public SearchConditionDto(
            CoordinateDto startCoordinate,
            double radiusRange,
            String criteria,
            String categories
    ) {
        this.startCoordinate = startCoordinate;
        this.radiusRange = radiusRange;
        this.criteria = criteria;
        this.categories = Arrays.asList(categories.split(SPLIT_DELIMITER));
    }
}
