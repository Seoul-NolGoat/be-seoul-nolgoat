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
    private List<String> categories;

    public SearchConditionDto(CoordinateDto startCoordinate, double radiusRange, String criteria, String categories) {
        this.startCoordinate = startCoordinate;
        this.radiusRange = radiusRange;
        this.criteria = criteria;
        this.setCategories(categories); // 문자열을 받아 리스트로 변환
    }

    private void setCategories(String categories) {
        this.categories = Arrays.asList(categories.split(SPLIT_DELIMITER));
    }
}
