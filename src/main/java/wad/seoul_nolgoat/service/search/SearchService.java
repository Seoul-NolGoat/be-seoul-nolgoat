package wad.seoul_nolgoat.service.search;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import wad.seoul_nolgoat.domain.store.StoreCategory;
import wad.seoul_nolgoat.domain.store.StoreType;
import wad.seoul_nolgoat.exception.ApplicationException;
import wad.seoul_nolgoat.exception.TMapException;
import wad.seoul_nolgoat.service.search.dto.SortConditionDto;
import wad.seoul_nolgoat.service.search.dto.StoreForPossibleCategoriesDto;
import wad.seoul_nolgoat.service.search.filter.FilterService;
import wad.seoul_nolgoat.service.search.sort.DistanceCalculator;
import wad.seoul_nolgoat.service.search.sort.SortService;
import wad.seoul_nolgoat.service.tMap.TMapService;
import wad.seoul_nolgoat.service.tMap.dto.WalkRouteInfoDto;
import wad.seoul_nolgoat.util.mapper.CombinationMapper;
import wad.seoul_nolgoat.web.search.dto.CoordinateDto;
import wad.seoul_nolgoat.web.search.dto.request.PossibleCategoriesConditionDto;
import wad.seoul_nolgoat.web.search.dto.request.SearchConditionDto;
import wad.seoul_nolgoat.web.search.dto.response.CombinationDto;

import java.util.*;
import java.util.stream.Collectors;

import static wad.seoul_nolgoat.exception.ErrorCode.INVALID_GATHERING_ROUND;
import static wad.seoul_nolgoat.exception.ErrorCode.INVALID_SEARCH_CRITERIA;

@Slf4j
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
        String criteria = searchConditionDto.getCriteria();
        if (criteria.equals(DISTANCE_CRITERIA)) {
            return getCombinationsByDistance(searchConditionDto);
        }
        if (criteria.equals(KAKAO_GRADE_CRITERIA)) {
            return getCombinationsByKakaoGrade(searchConditionDto);
        }
        if (criteria.equals(NOLGOAT_GRADE_CRITERIA)) {
            return getCombinationsByNolgoatGrade(searchConditionDto);
        }
        throw new ApplicationException(INVALID_SEARCH_CRITERIA);
    }

    public List<String> searchPossibleCategories(PossibleCategoriesConditionDto possibleCategoriesConditionDto) {
        List<StoreForPossibleCategoriesDto> untokenizedCategories = filterService.findCategoriesByRadiusRange(
                possibleCategoriesConditionDto.getStartCoordinate(),
                possibleCategoriesConditionDto.getRadiusRange()
        );

        Set<String> possibleCategories = new HashSet<>();
        for (StoreForPossibleCategoriesDto untokenizedCategory : untokenizedCategories) {
            if (!untokenizedCategory.storeType().equals(StoreType.RESTAURANT)) {
                possibleCategories.add(untokenizedCategory.storeType().toString());
                continue;
            }
            String[] tokens = untokenizedCategory.category().replace(SPACE, EMPTY).split(DELIMITER);
            for (String token : tokens) {
                Optional<String> primaryCategory = StoreCategory.findPrimaryCategoryName(token);
                primaryCategory.ifPresent(possibleCategories::add);
            }
        }

        return new ArrayList<>(possibleCategories);
    }

    private List<CombinationDto> getCombinationsByDistance(SearchConditionDto searchConditionDto) {
        List<String> categories = searchConditionDto.getCategories();
        int categoryCount = categories.size();
        CoordinateDto startCoordinate = searchConditionDto.getStartCoordinate();
        double radiusRange = searchConditionDto.getRadiusRange();

        if (categoryCount == THREE_ROUND) {
            List<CombinationDto> combinationDtos = sortService.sortStoresByDistance(
                            new SortConditionDto<>(
                                    startCoordinate,
                                    filterService.filterByRadiusRangeAndCategoryForDistanceSort(
                                            startCoordinate,
                                            radiusRange,
                                            categories.get(FIRST_CATEGORY)
                                    ),
                                    filterService.filterByRadiusRangeAndCategoryForDistanceSort(
                                            startCoordinate,
                                            radiusRange,
                                            categories.get(SECOND_CATEGORY)
                                    ),
                                    filterService.filterByRadiusRangeAndCategoryForDistanceSort(
                                            startCoordinate,
                                            radiusRange,
                                            categories.get(THIRD_CATEGORY)
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
        if (categoryCount == TWO_ROUND) {
            List<CombinationDto> combinationDtos = sortService.sortStoresByDistance(
                            new SortConditionDto<>(
                                    startCoordinate,
                                    filterService.filterByRadiusRangeAndCategoryForDistanceSort(
                                            startCoordinate,
                                            radiusRange,
                                            categories.get(FIRST_CATEGORY)
                                    ),
                                    filterService.filterByRadiusRangeAndCategoryForDistanceSort(
                                            startCoordinate,
                                            radiusRange,
                                            categories.get(SECOND_CATEGORY)
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
        if (categoryCount == ONE_ROUND) {
            List<CombinationDto> combinationDtos = sortService.sortStoresByDistance(
                            new SortConditionDto<>(
                                    startCoordinate,
                                    filterService.filterByRadiusRangeAndCategoryForDistanceSort(
                                            startCoordinate,
                                            radiusRange,
                                            categories.get(FIRST_CATEGORY)
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
        throw new ApplicationException(INVALID_GATHERING_ROUND);
    }

    private List<CombinationDto> getCombinationsByKakaoGrade(SearchConditionDto searchConditionDto) {
        List<String> categories = searchConditionDto.getCategories();
        int categoryCount = categories.size();
        CoordinateDto startCoordinate = searchConditionDto.getStartCoordinate();
        double radiusRange = searchConditionDto.getRadiusRange();

        if (categoryCount == THREE_ROUND) {
            List<CombinationDto> combinationDtos = sortService.sortStoresByGrade(
                            new SortConditionDto<>(
                                    startCoordinate,
                                    filterService.filterByRadiusRangeAndCategoryForKakaoGradeSort(
                                            startCoordinate,
                                            radiusRange,
                                            categories.get(FIRST_CATEGORY)
                                    ),
                                    filterService.filterByRadiusRangeAndCategoryForKakaoGradeSort(
                                            startCoordinate,
                                            radiusRange,
                                            categories.get(SECOND_CATEGORY)
                                    ),
                                    filterService.filterByRadiusRangeAndCategoryForKakaoGradeSort(
                                            startCoordinate,
                                            radiusRange,
                                            categories.get(THIRD_CATEGORY)
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
                    startCoordinate
            );
        }
        if (categoryCount == TWO_ROUND) {
            List<CombinationDto> combinationDtos = sortService.sortStoresByGrade(
                            new SortConditionDto<>(
                                    startCoordinate,
                                    filterService.filterByRadiusRangeAndCategoryForKakaoGradeSort(
                                            startCoordinate,
                                            radiusRange,
                                            categories.get(FIRST_CATEGORY)
                                    ),
                                    filterService.filterByRadiusRangeAndCategoryForKakaoGradeSort(
                                            startCoordinate,
                                            radiusRange,
                                            categories.get(SECOND_CATEGORY)
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
                    startCoordinate
            );
        }
        if (categoryCount == ONE_ROUND) {
            List<CombinationDto> combinationDtos = sortService.sortStoresByGrade(
                            new SortConditionDto<>(
                                    startCoordinate,
                                    filterService.filterByRadiusRangeAndCategoryForKakaoGradeSort(
                                            startCoordinate,
                                            radiusRange,
                                            categories.get(FIRST_CATEGORY)
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
                    startCoordinate
            );
        }
        throw new ApplicationException(INVALID_GATHERING_ROUND);
    }

    private List<CombinationDto> getCombinationsByNolgoatGrade(SearchConditionDto searchConditionDto) {
        List<String> categories = searchConditionDto.getCategories();
        int categoryCount = categories.size();
        CoordinateDto startCoordinate = searchConditionDto.getStartCoordinate();
        double radiusRange = searchConditionDto.getRadiusRange();

        if (categoryCount == THREE_ROUND) {
            List<CombinationDto> combinationDtos = sortService.sortStoresByGrade(
                            new SortConditionDto<>(
                                    startCoordinate,
                                    filterService.filterByRadiusRangeAndCategoryForNolgoatGradeSort(
                                            startCoordinate,
                                            radiusRange,
                                            categories.get(FIRST_CATEGORY)
                                    ),
                                    filterService.filterByRadiusRangeAndCategoryForNolgoatGradeSort(
                                            startCoordinate,
                                            radiusRange,
                                            categories.get(SECOND_CATEGORY)
                                    ),
                                    filterService.filterByRadiusRangeAndCategoryForNolgoatGradeSort(
                                            startCoordinate,
                                            radiusRange,
                                            categories.get(THIRD_CATEGORY)
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
                    startCoordinate
            );
        }
        if (categoryCount == TWO_ROUND) {
            List<CombinationDto> combinationDtos = sortService.sortStoresByGrade(
                            new SortConditionDto<>(
                                    startCoordinate,
                                    filterService.filterByRadiusRangeAndCategoryForNolgoatGradeSort(
                                            startCoordinate,
                                            radiusRange,
                                            categories.get(FIRST_CATEGORY)
                                    ),
                                    filterService.filterByRadiusRangeAndCategoryForNolgoatGradeSort(
                                            startCoordinate,
                                            radiusRange,
                                            categories.get(SECOND_CATEGORY)
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
                    startCoordinate
            );
        }
        if (categoryCount == ONE_ROUND) {
            List<CombinationDto> combinationDtos = sortService.sortStoresByGrade(
                            new SortConditionDto<>(
                                    startCoordinate,
                                    filterService.filterByRadiusRangeAndCategoryForNolgoatGradeSort(
                                            startCoordinate,
                                            radiusRange,
                                            categories.get(FIRST_CATEGORY)
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
                    startCoordinate
            );
        }
        throw new ApplicationException(INVALID_GATHERING_ROUND);
    }

    private List<CombinationDto> fetchWalkRouteInfoForCombinationDto(
            List<CombinationDto> combinationDtos,
            int totalRounds,
            CoordinateDto startCoordinate
    ) {
        return combinationDtos.stream()
                .map(combination -> {
                    try {
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
                        throw new ApplicationException(INVALID_GATHERING_ROUND);
                    } catch (TMapException e) {
                        log.info("TMap exception occurred", e);
                        int totalDistance = DistanceCalculator.calculateTotalDistanceForGradeWithFallback(
                                totalRounds,
                                combination,
                                startCoordinate
                        );
                        combination.setWalkRouteInfoDto(new WalkRouteInfoDto(totalDistance, TMapService.INVALID_TIME));

                        return combination;
                    }
                })
                .collect(Collectors.toList());
    }
}
