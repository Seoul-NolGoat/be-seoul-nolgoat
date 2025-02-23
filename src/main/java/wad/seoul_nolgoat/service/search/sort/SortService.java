package wad.seoul_nolgoat.service.search.sort;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import wad.seoul_nolgoat.exception.TMapException;
import wad.seoul_nolgoat.service.search.SearchService;
import wad.seoul_nolgoat.service.search.dto.*;
import wad.seoul_nolgoat.service.tMap.TMapService;
import wad.seoul_nolgoat.web.search.dto.CoordinateDto;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class SortService {

    private static final int TOTAL_GRADE_SORT_MAX_RESULT_COUNT = 10;
    private static final int TOTAL_DISTANCE_SORT_MAX_RESULT_COUNT = 20;

    private final TMapService tMapService;

    public List<GradeSortCombinationDto> sortStoresByGrade(SortConditionDto<StoreForGradeSortDto> sortConditionDto) {
        int totalRounds = sortConditionDto.totalRounds();
        List<GradeSortCombinationDto> gradeCombinations = generateGradeCombinations(sortConditionDto, totalRounds);
        sortCombinationsByGrade(gradeCombinations);
        List<GradeSortCombinationDto> filteredGradeCombinations = filterByBaseTotalGrade(gradeCombinations);

        return groupAndShuffleByGrade(filteredGradeCombinations);
    }

    public List<DistanceSortCombinationDto> sortStoresByDistance(SortConditionDto<StoreForDistanceSortDto> sortConditionDto) {
        int totalRounds = sortConditionDto.totalRounds();
        List<DistanceSortCombinationDto> distanceCombinations = generateAndSortDistanceCombinations(
                sortConditionDto,
                totalRounds
        );
        sortCombinationsByDistance(distanceCombinations);
        List<DistanceSortCombinationDto> combinationsUnderBaseDistance = filterByBaseTotalDistance(distanceCombinations);

        // TMap 도보 거리 측정
        List<DistanceSortCombinationDto> tMapFetchedDistanceCombinations;
        try {
            tMapFetchedDistanceCombinations = fetchDistancesFromTMapApi(
                    sortConditionDto.startCoordinate(),
                    combinationsUnderBaseDistance,
                    totalRounds
            );
        } catch (TMapException e) {
            log.info("TMap exception occurred", e);
            return groupAndShuffleByDistance(combinationsUnderBaseDistance);
        }

        return groupAndShuffleByTMapDistance(tMapFetchedDistanceCombinations);
    }

    // 테스트를 위해 접근제어자를 public으로 변경
    public List<DistanceSortCombinationDto> generateAndSortDistanceCombinations(
            SortConditionDto<StoreForDistanceSortDto> sortConditionDto,
            int totalRounds
    ) {
        if (totalRounds == SearchService.THREE_ROUND) {
            return createDistanceCombinationsForThreeRounds(
                    sortConditionDto.firstFilteredStores(),
                    sortConditionDto.secondFilteredStores(),
                    sortConditionDto.thirdFilteredStores(),
                    sortConditionDto.startCoordinate()
            );
        }
        if (totalRounds == SearchService.TWO_ROUND) {
            return createDistanceCombinationsForTwoRounds(
                    sortConditionDto.firstFilteredStores(),
                    sortConditionDto.secondFilteredStores(),
                    sortConditionDto.startCoordinate()
            );
        }

        // 1차인 경우
        return createDistanceCombinationsForOneRound(
                sortConditionDto.firstFilteredStores(),
                sortConditionDto.startCoordinate()
        );
    }

    private List<GradeSortCombinationDto> generateGradeCombinations(
            SortConditionDto<StoreForGradeSortDto> sortConditionDto,
            int totalRounds
    ) {
        if (totalRounds == SearchService.THREE_ROUND) {
            return createGradeCombinationsForThreeRounds(
                    sortConditionDto.firstFilteredStores(),
                    sortConditionDto.secondFilteredStores(),
                    sortConditionDto.thirdFilteredStores()
            );
        }
        if (totalRounds == SearchService.TWO_ROUND) {
            return createGradeCombinationsForTwoRounds(
                    sortConditionDto.firstFilteredStores(),
                    sortConditionDto.secondFilteredStores()
            );
        }

        // 1차인 경우
        return createGradeCombinationsForOneRound(sortConditionDto.firstFilteredStores());
    }

    private List<GradeSortCombinationDto> createGradeCombinationsForThreeRounds(
            List<StoreForGradeSortDto> firstStores,
            List<StoreForGradeSortDto> secondStores,
            List<StoreForGradeSortDto> thirdStores
    ) {
        List<GradeSortCombinationDto> gradeCombinations = new ArrayList<>();
        for (StoreForGradeSortDto firstStore : firstStores) {
            for (StoreForGradeSortDto secondStore : secondStores) {
                if (hasDuplicateStores(firstStore, secondStore)) {
                    continue;
                }
                for (StoreForGradeSortDto thirdStore : thirdStores) {
                    if (hasDuplicateStores(
                            firstStore,
                            secondStore,
                            thirdStore
                    )) {
                        continue;
                    }
                    gradeCombinations.add(
                            new GradeSortCombinationDto(
                                    firstStore,
                                    secondStore,
                                    thirdStore
                            )
                    );
                }
            }
        }

        return gradeCombinations;
    }

    private List<GradeSortCombinationDto> createGradeCombinationsForTwoRounds(
            List<StoreForGradeSortDto> firstStores,
            List<StoreForGradeSortDto> secondStores
    ) {
        List<GradeSortCombinationDto> gradeCombinations = new ArrayList<>();
        for (StoreForGradeSortDto firstStore : firstStores) {
            for (StoreForGradeSortDto secondStore : secondStores) {
                if (hasDuplicateStores(firstStore, secondStore)) {
                    continue;
                }
                gradeCombinations.add(new GradeSortCombinationDto(firstStore, secondStore));
            }
        }

        return gradeCombinations;
    }

    private List<GradeSortCombinationDto> createGradeCombinationsForOneRound(List<StoreForGradeSortDto> firstStores) {
        List<GradeSortCombinationDto> gradeCombinations = new ArrayList<>();
        for (StoreForGradeSortDto firstStore : firstStores) {
            gradeCombinations.add(new GradeSortCombinationDto(firstStore));
        }

        return gradeCombinations;
    }

    private void sortCombinationsByGrade(List<GradeSortCombinationDto> combinations) {
        combinations.sort((combination1, combination2) -> {
            double firstGrade = combination1.getTotalGrade();
            double secondGrade = combination2.getTotalGrade();

            return Double.compare(secondGrade, firstGrade);
        });
    }

    private void sortCombinationsByDistance(List<DistanceSortCombinationDto> distanceCombinations) {
        distanceCombinations.sort((combination1, combination2) -> {
            double firstDistance = combination1.getTotalDistance();
            double secondDistance = combination2.getTotalDistance();

            return Double.compare(firstDistance, secondDistance);
        });
    }

    // 10번째 조합의 총 평점을 기준으로 잡고 그 점수 이상인 조합들을 추출
    private List<GradeSortCombinationDto> filterByBaseTotalGrade(List<GradeSortCombinationDto> gradeCombinations) {
        int combinationCount = Math.min(gradeCombinations.size(), TOTAL_GRADE_SORT_MAX_RESULT_COUNT);
        double baseTotalGrade = gradeCombinations.get(combinationCount - 1).getTotalGrade();

        return gradeCombinations.stream()
                .filter(combination -> combination.getTotalGrade() >= baseTotalGrade)
                .toList();
    }

    // TMap 도보 거리 비교를 위해, 최대 20번째 조합의 총거리를 기준으로 필터링
    private List<DistanceSortCombinationDto> filterByBaseTotalDistance(List<DistanceSortCombinationDto> distanceCombinations) {
        int combinationCount = Math.min(distanceCombinations.size(), TOTAL_DISTANCE_SORT_MAX_RESULT_COUNT);
        double baseTotalDistance = distanceCombinations.get(combinationCount - 1).getTotalDistance();

        return distanceCombinations.stream()
                .filter(combination -> combination.getTotalDistance() <= baseTotalDistance)
                .toList();
    }

    // 평점별로 그룹화
    // 앞 순서의 가게가 상위권에 쏠리지 않도록 그룹내에서 순서를 무작위로 설정
    private List<GradeSortCombinationDto> groupAndShuffleByGrade(List<GradeSortCombinationDto> filteredGradeCombinations) {
        Map<Double, List<GradeSortCombinationDto>> groupedByGrade = filteredGradeCombinations.stream()
                .collect(
                        Collectors.groupingBy(
                                GradeSortCombinationDto::getTotalGrade,
                                LinkedHashMap::new,
                                Collectors.toList()
                        )
                );
        groupedByGrade.forEach((totalGrade, group) -> Collections.shuffle(group));

        return groupedByGrade.values().stream()
                .flatMap(List::stream)
                .toList();
    }

    // TMap 도보 거리 측정 결과로 그룹화
    // 앞 순서의 가게가 상위권에 쏠리지 않도록 그룹내에서 순서를 무작위로 설정
    private List<DistanceSortCombinationDto> groupAndShuffleByTMapDistance(List<DistanceSortCombinationDto> tMapFetchedDistanceCombinations) {
        Map<Integer, List<DistanceSortCombinationDto>> groupedByDistance = tMapFetchedDistanceCombinations.stream()
                .collect(
                        Collectors.groupingBy(
                                combination -> combination.getWalkRouteInfoDto().totalDistance(),
                                LinkedHashMap::new,
                                Collectors.toList()
                        )
                );
        groupedByDistance.forEach((totalDistance, group) -> Collections.shuffle(group));

        return groupedByDistance.values().stream()
                .flatMap(List::stream)
                .toList();
    }

    // 직선 거리 계산 결과로 그룹화
    // 앞 순서의 가게가 상위권에 쏠리지 않도록 그룹내에서 순서를 무작위로 설정
    private List<DistanceSortCombinationDto> groupAndShuffleByDistance(List<DistanceSortCombinationDto> combinationsUnderBaseDistance) {
        Map<Integer, List<DistanceSortCombinationDto>> groupedByDistance = combinationsUnderBaseDistance.stream()
                .collect(
                        Collectors.groupingBy(
                                DistanceSortCombinationDto::getTotalDistance,
                                LinkedHashMap::new,
                                Collectors.toList()
                        )
                );
        groupedByDistance.forEach((totalDistance, group) -> Collections.shuffle(group));

        return groupedByDistance.values().stream()
                .flatMap(List::stream)
                .toList();
    }

    private List<DistanceSortCombinationDto> createDistanceCombinationsForThreeRounds(
            List<StoreForDistanceSortDto> firstStores,
            List<StoreForDistanceSortDto> secondStores,
            List<StoreForDistanceSortDto> thirdStores,
            CoordinateDto coordinateDto
    ) {
        List<DistanceSortCombinationDto> combinations = new ArrayList<>();
        for (StoreForDistanceSortDto firstStore : firstStores) {
            for (StoreForDistanceSortDto secondStore : secondStores) {
                if (hasDuplicateStores(firstStore, secondStore)) {
                    continue;
                }
                for (StoreForDistanceSortDto thirdStore : thirdStores) {
                    if (hasDuplicateStores(
                            firstStore,
                            secondStore,
                            thirdStore
                    )) {
                        continue;
                    }
                    DistanceSortCombinationDto distanceSortCombinationDto = new DistanceSortCombinationDto(
                            firstStore,
                            secondStore,
                            thirdStore
                    );
                    int totalDistance = DistanceCalculator.calculateTotalDistance(
                            distanceSortCombinationDto.getFirstStore(),
                            distanceSortCombinationDto.getSecondStore(),
                            distanceSortCombinationDto.getThirdStore(),
                            coordinateDto
                    );
                    distanceSortCombinationDto.setTotalDistance(totalDistance);
                    combinations.add(distanceSortCombinationDto);
                }
            }
        }

        return combinations;
    }

    private List<DistanceSortCombinationDto> createDistanceCombinationsForTwoRounds(
            List<StoreForDistanceSortDto> firstStores,
            List<StoreForDistanceSortDto> secondStores,
            CoordinateDto coordinateDto
    ) {
        List<DistanceSortCombinationDto> combinations = new ArrayList<>();
        for (StoreForDistanceSortDto firstStore : firstStores) {
            for (StoreForDistanceSortDto secondStore : secondStores) {
                if (hasDuplicateStores(firstStore, secondStore)) {
                    continue;
                }
                DistanceSortCombinationDto distanceSortCombinationDto = new DistanceSortCombinationDto(
                        firstStore,
                        secondStore
                );
                int totalDistance = DistanceCalculator.calculateTotalDistance(
                        distanceSortCombinationDto.getFirstStore(),
                        distanceSortCombinationDto.getSecondStore(),
                        coordinateDto);
                distanceSortCombinationDto.setTotalDistance(totalDistance);
                combinations.add(distanceSortCombinationDto);
            }
        }

        return combinations;
    }

    private List<DistanceSortCombinationDto> createDistanceCombinationsForOneRound(
            List<StoreForDistanceSortDto> firstStores,
            CoordinateDto coordinateDto
    ) {
        List<DistanceSortCombinationDto> combinations = new ArrayList<>();
        for (StoreForDistanceSortDto firstStore : firstStores) {
            DistanceSortCombinationDto distanceSortCombinationDto = new DistanceSortCombinationDto(firstStore);
            int totalDistance = DistanceCalculator.calculateTotalDistance(
                    distanceSortCombinationDto.getFirstStore(), coordinateDto);
            distanceSortCombinationDto.setTotalDistance(totalDistance);
            combinations.add(distanceSortCombinationDto);
        }

        return combinations;
    }

    private List<DistanceSortCombinationDto> fetchDistancesFromTMapApi(
            CoordinateDto startCoordinate,
            List<DistanceSortCombinationDto> distanceSortCombinationDtos,
            int totalRounds
    ) {
        List<DistanceSortCombinationDto> combinations = distanceSortCombinationDtos.parallelStream()
                .map(combination -> {
                    if (totalRounds == SearchService.THREE_ROUND) {
                        CoordinateDto pass1 = combination.getFirstStore().coordinate();
                        CoordinateDto pass2 = combination.getSecondStore().coordinate();
                        CoordinateDto endCoordinate = combination.getThirdStore().coordinate();

                        return new DistanceSortCombinationDto(
                                combination.getFirstStore(),
                                combination.getSecondStore(),
                                combination.getThirdStore(),
                                tMapService.fetchFullPathWalkRouteInfo(
                                        startCoordinate,
                                        pass1,
                                        pass2,
                                        endCoordinate
                                )
                        );
                    }
                    if (totalRounds == SearchService.TWO_ROUND) {
                        CoordinateDto pass = combination.getFirstStore().coordinate();
                        CoordinateDto endCoordinate = combination.getSecondStore().coordinate();

                        return new DistanceSortCombinationDto(
                                combination.getFirstStore(),
                                combination.getSecondStore(),
                                tMapService.fetchFullPathWalkRouteInfo(
                                        startCoordinate,
                                        pass,
                                        endCoordinate
                                )
                        );
                    }

                    // 1차인 경우
                    CoordinateDto endCoordinate = combination.getFirstStore().coordinate();

                    return new DistanceSortCombinationDto(
                            combination.getFirstStore(),
                            tMapService.fetchFullPathWalkRouteInfo(startCoordinate, endCoordinate)
                    );
                }).toList();

        return combinations.stream()
                .sorted(Comparator.comparingInt(
                        combination -> combination.getWalkRouteInfoDto().totalDistance())
                )
                .toList();
    }

    private boolean hasDuplicateStores(
            StoreForDistanceSortDto firstStore,
            StoreForDistanceSortDto secondStore,
            StoreForDistanceSortDto thirdStore
    ) {
        Set<Long> storeIds = new HashSet<>();
        storeIds.add(firstStore.id());
        storeIds.add(secondStore.id());
        storeIds.add(thirdStore.id());

        return storeIds.size() != SearchService.THREE_ROUND;
    }

    private boolean hasDuplicateStores(StoreForDistanceSortDto firstStore, StoreForDistanceSortDto secondStore) {
        Set<Long> storeIds = new HashSet<>();
        storeIds.add(firstStore.id());
        storeIds.add(secondStore.id());

        return storeIds.size() != SearchService.TWO_ROUND;
    }

    private boolean hasDuplicateStores(
            StoreForGradeSortDto firstStore,
            StoreForGradeSortDto secondStore,
            StoreForGradeSortDto thirdStore
    ) {
        Set<Long> storeIds = new HashSet<>();
        storeIds.add(firstStore.id());
        storeIds.add(secondStore.id());
        storeIds.add(thirdStore.id());

        return storeIds.size() != SearchService.THREE_ROUND;
    }

    private boolean hasDuplicateStores(StoreForGradeSortDto firstStore, StoreForGradeSortDto secondStore) {
        Set<Long> storeIds = new HashSet<>();
        storeIds.add(firstStore.id());
        storeIds.add(secondStore.id());

        return storeIds.size() != SearchService.TWO_ROUND;
    }
}
