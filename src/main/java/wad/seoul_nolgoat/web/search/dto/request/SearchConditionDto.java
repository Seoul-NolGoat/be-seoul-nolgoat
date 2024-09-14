package wad.seoul_nolgoat.web.search.dto.request;

import lombok.Getter;
import wad.seoul_nolgoat.web.search.dto.CoordinateDto;

import java.util.Arrays;
import java.util.List;

@Getter
public class SearchConditionDto {

    private static final String SPLIT_DELIMITER = ";";

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

    // 클라이언트에서 api를 호출할 때: 쉼표가 포함되는 값이 있다(ex. 육류,고기). 쉼표를 구분자로 하여 categories를 입력하면 값에 포함되는 쉼표가 구분자로 인식되어 오류가 발생할 수도 있다(ex. "육류,고기"를 "육류""고기"로 인식할 수도 있음). 때문에 구분자로 ; 를 이용하였다.
    private void setCategories(String categories) {
        this.categories = Arrays.stream(categories.split(SPLIT_DELIMITER))
                .map(String::trim)
                .toList();
    }
}
