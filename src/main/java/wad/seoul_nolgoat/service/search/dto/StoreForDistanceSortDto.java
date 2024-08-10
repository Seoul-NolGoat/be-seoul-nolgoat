package wad.seoul_nolgoat.service.search.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wad.seoul_nolgoat.web.search.dto.CoordinateDto;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StoreForDistanceSortDto {

    private Long id;
    private String name;
    private CoordinateDto coordinate;
    private double kakaoAverageGrade;
    private double nolgoatAverageGrade;
}
