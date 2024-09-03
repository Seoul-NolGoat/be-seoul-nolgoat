package wad.seoul_nolgoat.service.search;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import wad.seoul_nolgoat.domain.store.StoreCategory;
import wad.seoul_nolgoat.exception.ErrorMessages;
import wad.seoul_nolgoat.exception.InvalidRoundException;
import wad.seoul_nolgoat.exception.InvalidSearchCriteriaException;
import wad.seoul_nolgoat.service.search.dto.SortConditionDto;
import wad.seoul_nolgoat.service.search.filter.FilterService;
import wad.seoul_nolgoat.service.search.sort.SortService;
import wad.seoul_nolgoat.service.tMap.TMapService;
import wad.seoul_nolgoat.util.mapper.CombinationMapper;
import wad.seoul_nolgoat.web.search.dto.CoordinateDto;
import wad.seoul_nolgoat.web.search.dto.request.PossibleCategoriesConditionDto;
import wad.seoul_nolgoat.web.search.dto.request.SearchConditionDto;
import wad.seoul_nolgoat.web.search.dto.response.CombinationDto;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class SearchService {

    public static final int ONE_ROUND = 1;
    public static final int TWO_ROUND = 2;
    public static final int THREE_ROUND = 3;

    private static final String DISTANCE_CRITERIA = "distance";
    private static final String KAKAO_GRADE_CRITERIA = "kakaoGrade";
    private static final String NOLGOAT_GRADE_CRITERIA = "nolgoatGrade";

    private static final int FIRST_CATEGORY = 0;
    private static final int SECOND_CATEGORY = 1;
    private static final int THIRD_CATEGORY = 2;

    private static final int STORE_COMBINATION_SEARCH_START = 0;
    private static final int STORE_COMBINATION_SEARCH_LIMIT = 10;

    private static final String SPACE = " ";
    private static final String EMPTY = "";
    private static final String DELIMITER = ">";

    private final FilterService filterService;
    private final SortService sortService;
    private final TMapService tMapService;

    public List<CombinationDto> searchAll(SearchConditionDto searchConditionDto) {
        if (searchConditionDto.getCriteria().equals(DISTANCE_CRITERIA)) {
            return getCombinationsByDistance(searchConditionDto);
        }
        if (searchConditionDto.getCriteria().equals(KAKAO_GRADE_CRITERIA)) {
            return getCombinationsByKakaoGrade(searchConditionDto);
        }
        if (searchConditionDto.getCriteria().equals(NOLGOAT_GRADE_CRITERIA)) {
            return getCombinationsByNolgoatGrade(searchConditionDto);
        }
        throw new InvalidSearchCriteriaException(ErrorMessages.INVALID_SEARCH_CRITERIA);
    }

    public List<String> searchPossibleCategories(PossibleCategoriesConditionDto possibleCategoriesConditionDto) {
        List<String> untokenizedCategories = filterService.findCategoriesByRadiusRange(
                possibleCategoriesConditionDto.getStartCoordinate(),
                possibleCategoriesConditionDto.getRadiusRange()
        );
        Set<String> possibleCategories = new HashSet<>();

        for (String untokenizedCategory : untokenizedCategories) {
            String[] tokens = untokenizedCategory.replace(SPACE, EMPTY).split(DELIMITER);
            for (String token : tokens) {
                Optional<String[]> optionalRelatedCategories = StoreCategory.findRelatedCategoryNames(token);
                optionalRelatedCategories.ifPresent(
                        relatedCategories -> possibleCategories.addAll(Arrays.asList(relatedCategories))
                );
            }
        }

        return new ArrayList<>(possibleCategories);
    }

    private List<CombinationDto> getCombinationsByDistance(SearchConditionDto searchConditionDto) {
        if (searchConditionDto.getCategories().size() == THREE_ROUND) {
            List<CombinationDto> combinationDtos = sortService.sortStoresByDistance(
                            new SortConditionDto<>(
                                    searchConditionDto.getStartCoordinate(),
                                    filterService.filterByRadiusRangeAndCategoryForDistanceSort(
                                            searchConditionDto.getStartCoordinate(),
                                            searchConditionDto.getRadiusRange(),
                                            searchConditionDto.getCategories().get(FIRST_CATEGORY)
                                    ),
                                    filterService.filterByRadiusRangeAndCategoryForDistanceSort(
                                            searchConditionDto.getStartCoordinate(),
                                            searchConditionDto.getRadiusRange(),
                                            searchConditionDto.getCategories().get(SECOND_CATEGORY)
                                    ),
                                    filterService.filterByRadiusRangeAndCategoryForDistanceSort(
                                            searchConditionDto.getStartCoordinate(),
                                            searchConditionDto.getRadiusRange(),
                                            searchConditionDto.getCategories().get(THIRD_CATEGORY)
                                    )
                            )
                    ).stream()
                    .map(CombinationMapper::toCombinationDto)
                    .toList();

            return combinationDtos.subList(
                    STORE_COMBINATION_SEARCH_START,
                    Math.min(STORE_COMBINATION_SEARCH_LIMIT, combinationDtos.size())
            );
        }
        if (searchConditionDto.getCategories().size() == TWO_ROUND) {
            List<CombinationDto> combinationDtos = sortService.sortStoresByDistance(
                            new SortConditionDto<>(
                                    searchConditionDto.getStartCoordinate(),
                                    filterService.filterByRadiusRangeAndCategoryForDistanceSort(
                                            searchConditionDto.getStartCoordinate(),
                                            searchConditionDto.getRadiusRange(),
                                            searchConditionDto.getCategories().get(FIRST_CATEGORY)
                                    ),
                                    filterService.filterByRadiusRangeAndCategoryForDistanceSort(
                                            searchConditionDto.getStartCoordinate(),
                                            searchConditionDto.getRadiusRange(),
                                            searchConditionDto.getCategories().get(SECOND_CATEGORY)
                                    )
                            )
                    ).stream()
                    .map(CombinationMapper::toCombinationDto)
                    .toList();

            return combinationDtos.subList(
                    STORE_COMBINATION_SEARCH_START,
                    Math.min(STORE_COMBINATION_SEARCH_LIMIT, combinationDtos.size())
            );
        }
        if (searchConditionDto.getCategories().size() == ONE_ROUND) {
            List<CombinationDto> combinationDtos = sortService.sortStoresByDistance(
                            new SortConditionDto<>(
                                    searchConditionDto.getStartCoordinate(),
                                    filterService.filterByRadiusRangeAndCategoryForDistanceSort(
                                            searchConditionDto.getStartCoordinate(),
                                            searchConditionDto.getRadiusRange(),
                                            searchConditionDto.getCategories().get(FIRST_CATEGORY)
                                    )
                            )
                    ).stream()
                    .map(CombinationMapper::toCombinationDto)
                    .toList();

            return combinationDtos.subList(
                    STORE_COMBINATION_SEARCH_START,
                    Math.min(STORE_COMBINATION_SEARCH_LIMIT, combinationDtos.size())
            );
        }
        throw new InvalidRoundException(ErrorMessages.INVALID_GATHERING_ROUND);
    }

    private List<CombinationDto> getCombinationsByKakaoGrade(SearchConditionDto searchConditionDto) {
        if (searchConditionDto.getCategories().size() == THREE_ROUND) {
            List<CombinationDto> combinationDtos = sortService.sortStoresByGrade(
                            new SortConditionDto<>(
                                    searchConditionDto.getStartCoordinate(),
                                    filterService.filterByRadiusRangeAndCategoryForKakaoGradeSort(
                                            searchConditionDto.getStartCoordinate(),
                                            searchConditionDto.getRadiusRange(),
                                            searchConditionDto.getCategories().get(FIRST_CATEGORY)
                                    ),
                                    filterService.filterByRadiusRangeAndCategoryForKakaoGradeSort(
                                            searchConditionDto.getStartCoordinate(),
                                            searchConditionDto.getRadiusRange(),
                                            searchConditionDto.getCategories().get(SECOND_CATEGORY)
                                    ),
                                    filterService.filterByRadiusRangeAndCategoryForKakaoGradeSort(
                                            searchConditionDto.getStartCoordinate(),
                                            searchConditionDto.getRadiusRange(),
                                            searchConditionDto.getCategories().get(THIRD_CATEGORY)
                                    )
                            )
                    ).stream()
                    .map(CombinationMapper::toCombinationDto)
                    .toList();

            return fetchWalkRouteInfoForCombinationDto(
                    combinationDtos.subList(
                            STORE_COMBINATION_SEARCH_START,
                            Math.min(STORE_COMBINATION_SEARCH_LIMIT, combinationDtos.size())
                    ),
                    THREE_ROUND,
                    searchConditionDto.getStartCoordinate()
            );
        }
        if (searchConditionDto.getCategories().size() == TWO_ROUND) {
            List<CombinationDto> combinationDtos = sortService.sortStoresByGrade(
                            new SortConditionDto<>(
                                    searchConditionDto.getStartCoordinate(),
                                    filterService.filterByRadiusRangeAndCategoryForKakaoGradeSort(
                                            searchConditionDto.getStartCoordinate(),
                                            searchConditionDto.getRadiusRange(),
                                            searchConditionDto.getCategories().get(FIRST_CATEGORY)
                                    ),
                                    filterService.filterByRadiusRangeAndCategoryForKakaoGradeSort(
                                            searchConditionDto.getStartCoordinate(),
                                            searchConditionDto.getRadiusRange(),
                                            searchConditionDto.getCategories().get(SECOND_CATEGORY)
                                    )
                            )
                    ).stream()
                    .map(CombinationMapper::toCombinationDto)
                    .toList();

            return fetchWalkRouteInfoForCombinationDto(
                    combinationDtos.subList(
                            STORE_COMBINATION_SEARCH_START,
                            Math.min(STORE_COMBINATION_SEARCH_LIMIT, combinationDtos.size())
                    ),
                    TWO_ROUND,
                    searchConditionDto.getStartCoordinate()
            );
        }
        if (searchConditionDto.getCategories().size() == ONE_ROUND) {
            List<CombinationDto> combinationDtos = sortService.sortStoresByGrade(
                            new SortConditionDto<>(
                                    searchConditionDto.getStartCoordinate(),
                                    filterService.filterByRadiusRangeAndCategoryForKakaoGradeSort(
                                            searchConditionDto.getStartCoordinate(),
                                            searchConditionDto.getRadiusRange(),
                                            searchConditionDto.getCategories().get(FIRST_CATEGORY)
                                    )
                            )
                    ).stream()
                    .map(CombinationMapper::toCombinationDto)
                    .toList();

            return fetchWalkRouteInfoForCombinationDto(
                    combinationDtos.subList(
                            STORE_COMBINATION_SEARCH_START,
                            Math.min(STORE_COMBINATION_SEARCH_LIMIT, combinationDtos.size())
                    ),
                    ONE_ROUND,
                    searchConditionDto.getStartCoordinate()
            );
        }
        throw new InvalidRoundException(ErrorMessages.INVALID_GATHERING_ROUND);
    }

    private List<CombinationDto> getCombinationsByNolgoatGrade(SearchConditionDto searchConditionDto) {
        if (searchConditionDto.getCategories().size() == THREE_ROUND) {
            List<CombinationDto> combinationDtos = sortService.sortStoresByGrade(
                            new SortConditionDto<>(
                                    searchConditionDto.getStartCoordinate(),
                                    filterService.filterByRadiusRangeAndCategoryForNolgoatGradeSort(
                                            searchConditionDto.getStartCoordinate(),
                                            searchConditionDto.getRadiusRange(),
                                            searchConditionDto.getCategories().get(FIRST_CATEGORY)
                                    ),
                                    filterService.filterByRadiusRangeAndCategoryForNolgoatGradeSort(
                                            searchConditionDto.getStartCoordinate(),
                                            searchConditionDto.getRadiusRange(),
                                            searchConditionDto.getCategories().get(SECOND_CATEGORY)
                                    ),
                                    filterService.filterByRadiusRangeAndCategoryForNolgoatGradeSort(
                                            searchConditionDto.getStartCoordinate(),
                                            searchConditionDto.getRadiusRange(),
                                            searchConditionDto.getCategories().get(THIRD_CATEGORY)
                                    )
                            )
                    ).stream()
                    .map(CombinationMapper::toCombinationDto)
                    .toList();

            return fetchWalkRouteInfoForCombinationDto(
                    combinationDtos.subList(
                            STORE_COMBINATION_SEARCH_START,
                            Math.min(STORE_COMBINATION_SEARCH_LIMIT, combinationDtos.size())
                    ),
                    THREE_ROUND,
                    searchConditionDto.getStartCoordinate()
            );
        }
        if (searchConditionDto.getCategories().size() == TWO_ROUND) {
            List<CombinationDto> combinationDtos = sortService.sortStoresByGrade(
                            new SortConditionDto<>(
                                    searchConditionDto.getStartCoordinate(),
                                    filterService.filterByRadiusRangeAndCategoryForNolgoatGradeSort(
                                            searchConditionDto.getStartCoordinate(),
                                            searchConditionDto.getRadiusRange(),
                                            searchConditionDto.getCategories().get(FIRST_CATEGORY)
                                    ),
                                    filterService.filterByRadiusRangeAndCategoryForNolgoatGradeSort(
                                            searchConditionDto.getStartCoordinate(),
                                            searchConditionDto.getRadiusRange(),
                                            searchConditionDto.getCategories().get(SECOND_CATEGORY)
                                    )
                            )
                    ).stream()
                    .map(CombinationMapper::toCombinationDto)
                    .toList();

            return fetchWalkRouteInfoForCombinationDto(
                    combinationDtos.subList(
                            STORE_COMBINATION_SEARCH_START,
                            Math.min(STORE_COMBINATION_SEARCH_LIMIT, combinationDtos.size())
                    ),
                    TWO_ROUND,
                    searchConditionDto.getStartCoordinate()
            );
        }
        if (searchConditionDto.getCategories().size() == ONE_ROUND) {
            List<CombinationDto> combinationDtos = sortService.sortStoresByGrade(
                            new SortConditionDto<>(
                                    searchConditionDto.getStartCoordinate(),
                                    filterService.filterByRadiusRangeAndCategoryForNolgoatGradeSort(
                                            searchConditionDto.getStartCoordinate(),
                                            searchConditionDto.getRadiusRange(),
                                            searchConditionDto.getCategories().get(FIRST_CATEGORY)
                                    )
                            )
                    ).stream()
                    .map(CombinationMapper::toCombinationDto)
                    .toList();

            return fetchWalkRouteInfoForCombinationDto(
                    combinationDtos.subList(
                            STORE_COMBINATION_SEARCH_START,
                            Math.min(STORE_COMBINATION_SEARCH_LIMIT, combinationDtos.size())
                    ),
                    ONE_ROUND,
                    searchConditionDto.getStartCoordinate()
            );
        }
        throw new InvalidRoundException(ErrorMessages.INVALID_GATHERING_ROUND);
    }

    private List<CombinationDto> fetchWalkRouteInfoForCombinationDto(
            List<CombinationDto> combinationDtos,
            int totalRounds,
            CoordinateDto startCoordinate) {
        return combinationDtos.stream()
                .map(combination -> {
                    if (totalRounds == THREE_ROUND) {
                        CoordinateDto pass1 = combination.getFirstStore().getCoordinate();
                        CoordinateDto pass2 = combination.getSecondStore().getCoordinate();
                        CoordinateDto endCoordinate = combination.getThirdStore().getCoordinate();
                        combination.setWalkRouteInfoDto(tMapService.fetchWalkRouteInfo(
                                startCoordinate,
                                pass1,
                                pass2,
                                endCoordinate
                        ));

                        return combination;
                    }
                    if (totalRounds == TWO_ROUND) {
                        CoordinateDto pass = combination.getFirstStore().getCoordinate();
                        CoordinateDto endCoordinate = combination.getSecondStore().getCoordinate();
                        combination.setWalkRouteInfoDto(tMapService.fetchWalkRouteInfo(
                                startCoordinate,
                                pass,
                                endCoordinate
                        ));

                        return combination;
                    }
                    if (totalRounds == ONE_ROUND) {
                        CoordinateDto endCoordinate = combination.getFirstStore().getCoordinate();
                        combination.setWalkRouteInfoDto(tMapService.fetchWalkRouteInfo(
                                startCoordinate,
                                endCoordinate
                        ));

                        return combination;
                    }
                    throw new InvalidRoundException(ErrorMessages.INVALID_GATHERING_ROUND);
                })
                .collect(Collectors.toList());
    }
}
