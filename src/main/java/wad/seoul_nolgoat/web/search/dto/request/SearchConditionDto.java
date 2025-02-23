package wad.seoul_nolgoat.web.search.dto.request;

import wad.seoul_nolgoat.web.search.dto.CoordinateDto;

import java.util.Arrays;
import java.util.List;

public record SearchConditionDto(
        CoordinateDto startCoordinate,
        double radiusRange,
        String criteria,
        List<String> categories
) {
    private static final String SPLIT_DELIMITER = ",";

    public SearchConditionDto(
            CoordinateDto startCoordinate,
            double radiusRange,
            String criteria,
            String categories
    ) {
        this(
                startCoordinate,
                radiusRange,
                criteria,
                Arrays.asList(categories.split(SPLIT_DELIMITER))
        );
    }
}
