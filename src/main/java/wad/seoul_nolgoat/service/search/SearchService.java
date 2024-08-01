package wad.seoul_nolgoat.service.search;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import wad.seoul_nolgoat.service.search.dto.SortConditionDto;
import wad.seoul_nolgoat.service.search.filter.FilterService;
import wad.seoul_nolgoat.service.search.sort.SortService;
import wad.seoul_nolgoat.util.mapper.CombinationMapper;
import wad.seoul_nolgoat.web.search.dto.request.SearchConditionDto;
import wad.seoul_nolgoat.web.search.dto.response.CombinationDto;

import java.util.List;

@RequiredArgsConstructor
@Service
public class SearchService {

    private final FilterService filterService;
    private final SortService sortService;

    public List<CombinationDto> searchAll(SearchConditionDto searchConditionDto) {
        if (searchConditionDto.getCriteria().equals("distance")) {
            return getCombinationsByDistance(searchConditionDto);
        }
        if (searchConditionDto.getCriteria().equals("kakaoGrade")) {
            return getCombinationsByKakaoGrade(searchConditionDto);
        }
        if (searchConditionDto.getCriteria().equals("nolgoatGrade")) {
            return getCombinationsByNolgoatGrade(searchConditionDto);
        }
        throw new RuntimeException();
    }

    private List<CombinationDto> getCombinationsByDistance(SearchConditionDto searchConditionDto) {
        if (searchConditionDto.getCategories().size() == 3) {
            return sortService.sortStoresByDistance(
                            new SortConditionDto<>(
                                    searchConditionDto.getStartCoordinate(),
                                    filterService.filterByRadiusRangeAndCategoryForDistanceSort(
                                            searchConditionDto.getStartCoordinate(),
                                            searchConditionDto.getRadiusRange(),
                                            searchConditionDto.getCategories().get(0)
                                    ),
                                    filterService.filterByRadiusRangeAndCategoryForDistanceSort(
                                            searchConditionDto.getStartCoordinate(),
                                            searchConditionDto.getRadiusRange(),
                                            searchConditionDto.getCategories().get(1)
                                    ),
                                    filterService.filterByRadiusRangeAndCategoryForDistanceSort(
                                            searchConditionDto.getStartCoordinate(),
                                            searchConditionDto.getRadiusRange(),
                                            searchConditionDto.getCategories().get(2)
                                    )
                            )
                    ).stream()
                    .map(CombinationMapper::toCombinationDto)
                    .toList();
        }
        if (searchConditionDto.getCategories().size() == 2) {
            return sortService.sortStoresByDistance(
                            new SortConditionDto<>(
                                    searchConditionDto.getStartCoordinate(),
                                    filterService.filterByRadiusRangeAndCategoryForDistanceSort(
                                            searchConditionDto.getStartCoordinate(),
                                            searchConditionDto.getRadiusRange(),
                                            searchConditionDto.getCategories().get(0)
                                    ),
                                    filterService.filterByRadiusRangeAndCategoryForDistanceSort(
                                            searchConditionDto.getStartCoordinate(),
                                            searchConditionDto.getRadiusRange(),
                                            searchConditionDto.getCategories().get(1)
                                    )
                            )
                    ).stream()
                    .map(CombinationMapper::toCombinationDto)
                    .toList();
        }
        if (searchConditionDto.getCategories().size() == 1) {
            return sortService.sortStoresByDistance(
                            new SortConditionDto<>(
                                    searchConditionDto.getStartCoordinate(),
                                    filterService.filterByRadiusRangeAndCategoryForDistanceSort(
                                            searchConditionDto.getStartCoordinate(),
                                            searchConditionDto.getRadiusRange(),
                                            searchConditionDto.getCategories().get(0)
                                    )
                            )
                    ).stream()
                    .map(CombinationMapper::toCombinationDto)
                    .toList();
        }
        throw new RuntimeException();
    }

    private List<CombinationDto> getCombinationsByKakaoGrade(SearchConditionDto searchConditionDto) {
        if (searchConditionDto.getCategories().size() == 3) {
            return sortService.sortStoresByGrade(
                            new SortConditionDto<>(
                                    searchConditionDto.getStartCoordinate(),
                                    filterService.filterByRadiusRangeAndCategoryForKakaoGradeSort(
                                            searchConditionDto.getStartCoordinate(),
                                            searchConditionDto.getRadiusRange(),
                                            searchConditionDto.getCategories().get(0)
                                    ),
                                    filterService.filterByRadiusRangeAndCategoryForKakaoGradeSort(
                                            searchConditionDto.getStartCoordinate(),
                                            searchConditionDto.getRadiusRange(),
                                            searchConditionDto.getCategories().get(1)
                                    ),
                                    filterService.filterByRadiusRangeAndCategoryForKakaoGradeSort(
                                            searchConditionDto.getStartCoordinate(),
                                            searchConditionDto.getRadiusRange(),
                                            searchConditionDto.getCategories().get(2)
                                    )
                            )
                    ).stream()
                    .map(CombinationMapper::toCombinationDto)
                    .toList();
        }
        if (searchConditionDto.getCategories().size() == 2) {
            return sortService.sortStoresByGrade(
                            new SortConditionDto<>(
                                    searchConditionDto.getStartCoordinate(),
                                    filterService.filterByRadiusRangeAndCategoryForKakaoGradeSort(
                                            searchConditionDto.getStartCoordinate(),
                                            searchConditionDto.getRadiusRange(),
                                            searchConditionDto.getCategories().get(0)
                                    ),
                                    filterService.filterByRadiusRangeAndCategoryForKakaoGradeSort(
                                            searchConditionDto.getStartCoordinate(),
                                            searchConditionDto.getRadiusRange(),
                                            searchConditionDto.getCategories().get(1)
                                    )
                            )
                    ).stream()
                    .map(CombinationMapper::toCombinationDto)
                    .toList();
        }
        if (searchConditionDto.getCategories().size() == 1) {
            return sortService.sortStoresByGrade(
                            new SortConditionDto<>(
                                    searchConditionDto.getStartCoordinate(),
                                    filterService.filterByRadiusRangeAndCategoryForKakaoGradeSort(
                                            searchConditionDto.getStartCoordinate(),
                                            searchConditionDto.getRadiusRange(),
                                            searchConditionDto.getCategories().get(0)
                                    )
                            )
                    ).stream()
                    .map(CombinationMapper::toCombinationDto)
                    .toList();
        }
        throw new RuntimeException();
    }

    private List<CombinationDto> getCombinationsByNolgoatGrade(SearchConditionDto searchConditionDto) {
        if (searchConditionDto.getCategories().size() == 3) {
            return sortService.sortStoresByGrade(
                            new SortConditionDto<>(
                                    searchConditionDto.getStartCoordinate(),
                                    filterService.filterByRadiusRangeAndCategoryForNolgoatGradeSort(
                                            searchConditionDto.getStartCoordinate(),
                                            searchConditionDto.getRadiusRange(),
                                            searchConditionDto.getCategories().get(0)
                                    ),
                                    filterService.filterByRadiusRangeAndCategoryForNolgoatGradeSort(
                                            searchConditionDto.getStartCoordinate(),
                                            searchConditionDto.getRadiusRange(),
                                            searchConditionDto.getCategories().get(1)
                                    ),
                                    filterService.filterByRadiusRangeAndCategoryForNolgoatGradeSort(
                                            searchConditionDto.getStartCoordinate(),
                                            searchConditionDto.getRadiusRange(),
                                            searchConditionDto.getCategories().get(2)
                                    )
                            )
                    ).stream()
                    .map(CombinationMapper::toCombinationDto)
                    .toList();
        }
        if (searchConditionDto.getCategories().size() == 2) {
            return sortService.sortStoresByGrade(
                            new SortConditionDto<>(
                                    searchConditionDto.getStartCoordinate(),
                                    filterService.filterByRadiusRangeAndCategoryForNolgoatGradeSort(
                                            searchConditionDto.getStartCoordinate(),
                                            searchConditionDto.getRadiusRange(),
                                            searchConditionDto.getCategories().get(0)
                                    ),
                                    filterService.filterByRadiusRangeAndCategoryForNolgoatGradeSort(
                                            searchConditionDto.getStartCoordinate(),
                                            searchConditionDto.getRadiusRange(),
                                            searchConditionDto.getCategories().get(1)
                                    )
                            )
                    ).stream()
                    .map(CombinationMapper::toCombinationDto)
                    .toList();
        }
        if (searchConditionDto.getCategories().size() == 1) {
            return sortService.sortStoresByGrade(
                            new SortConditionDto<>(
                                    searchConditionDto.getStartCoordinate(),
                                    filterService.filterByRadiusRangeAndCategoryForNolgoatGradeSort(
                                            searchConditionDto.getStartCoordinate(),
                                            searchConditionDto.getRadiusRange(),
                                            searchConditionDto.getCategories().get(0)
                                    )
                            )
                    ).stream()
                    .map(CombinationMapper::toCombinationDto)
                    .toList();
        }
        throw new RuntimeException();
    }
}
