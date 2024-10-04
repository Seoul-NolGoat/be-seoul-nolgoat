package wad.seoul_nolgoat.service.search.sort;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import wad.seoul_nolgoat.exception.search.InvalidRoundException;
import wad.seoul_nolgoat.service.search.SearchService;
import wad.seoul_nolgoat.service.search.dto.*;
import wad.seoul_nolgoat.service.tMap.TMapService;
import wad.seoul_nolgoat.web.search.dto.CoordinateDto;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class SortService {

    private static final int TOP_FIRST = 0;
    private static final int TOP_TWENTIETH = 20;

    private final TMapService tMapService;
    private final DistanceCalculator distanceCalculator;

    public List<GradeSortCombinationDto> sortStoresByGrade(SortConditionDto<StoreForGradeSortDto> sortConditionDto) {
        int totalRounds = sortConditionDto.getTotalRounds();
        List<GradeSortCombinationDto> gradeCombinations = generateGradeCombinations(sortConditionDto, totalRounds);
        sortCombinationsByGrade(gradeCombinations);
        List<GradeSortCombinationDto> filteredGradeCombinations = filterByBaseTotalGrade(gradeCombinations);

        return groupAndShuffleByGrade(filteredGradeCombinations);
    }

    public List<DistanceSortCombinationDto> sortStoresByDistance(SortConditionDto<StoreForDistanceSortDto> sortConditionDto) {
        int totalRounds = sortConditionDto.getTotalRounds();
        List<DistanceSortCombinationDto> distanceCombinations = generateAndSortDistanceCombinations(
                sortConditionDto,
                totalRounds
        );

        return fetchDistancesFromTMapApi(
                sortConditionDto.getStartCoordinate(),
                distanceCombinations.subList(TOP_FIRST, Math.min(distanceCombinations.size(), TOP_TWENTIETH)),
                totalRounds
        );
    }

    // 테스트를 위해 접근제어자를 public으로 변경
    public List<DistanceSortCombinationDto> generateAndSortDistanceCombinations(
            SortConditionDto<StoreForDistanceSortDto> sortConditionDto,
            int totalRounds
    ) {
        if (totalRounds == SearchService.THREE_ROUND) {
            return createDistanceCombinationsForThreeRounds(
                    sortConditionDto.getFirstFilteredStores(),
                    sortConditionDto.getSecondFilteredStores(),
                    sortConditionDto.getThirdFilteredStores(),
                    sortConditionDto.getStartCoordinate()
            ).stream()
                    .sorted(Comparator.comparingDouble(DistanceSortCombinationDto::getTotalDistance))
                    .toList();
        }
        if (totalRounds == SearchService.TWO_ROUND) {
            return createDistanceCombinationsForTwoRounds(
                    sortConditionDto.getFirstFilteredStores(),
                    sortConditionDto.getSecondFilteredStores(),
                    sortConditionDto.getStartCoordinate()
            ).stream()
                    .sorted(Comparator.comparingDouble(DistanceSortCombinationDto::getTotalDistance))
                    .toList();
        }
        if (totalRounds == SearchService.ONE_ROUND) {
            return createDistanceCombinationsForOneRound(
                    sortConditionDto.getFirstFilteredStores(),
                    sortConditionDto.getStartCoordinate()
            ).stream()
                    .sorted(Comparator.comparingDouble(DistanceSortCombinationDto::getTotalDistance))
                    .toList();
        }
        throw new InvalidRoundException();
    }

    private List<GradeSortCombinationDto> generateGradeCombinations(
            SortConditionDto<StoreForGradeSortDto> sortConditionDto,
            int totalRounds
    ) {
        if (totalRounds == SearchService.THREE_ROUND) {
            return createGradeCombinationsForThreeRounds(
                    sortConditionDto.getFirstFilteredStores(),
                    sortConditionDto.getSecondFilteredStores(),
                    sortConditionDto.getThirdFilteredStores()
            );
        }
        if (totalRounds == SearchService.TWO_ROUND) {
            return createGradeCombinationsForTwoRounds(
                    sortConditionDto.getFirstFilteredStores(),
                    sortConditionDto.getSecondFilteredStores()
            );
        }
        if (totalRounds == SearchService.ONE_ROUND) {
            return createGradeCombinationsForOneRound(sortConditionDto.getFirstFilteredStores());
        }
        throw new InvalidRoundException();
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
            double firstRate = combination1.getTotalGrade();
            double secondRate = combination2.getTotalGrade();

            return Double.compare(secondRate, firstRate);
        });
    }

    // 10번째 조합의 총 평점을 기준으로 잡고 그 점수 이상인 조합들을 추출
    private List<GradeSortCombinationDto> filterByBaseTotalGrade(List<GradeSortCombinationDto> gradeCombinations) {
        double baseTotalGrade = gradeCombinations.get(Math.min(9, gradeCombinations.size())).getTotalGrade();

        return gradeCombinations.stream()
                .filter(combination -> combination.getTotalGrade() >= baseTotalGrade)
                .toList();
    }

    // 평점별로 그룹화
    // 앞 순서의 가게가 상위권에 쏠리지 않도록 그룹내에서 순서를 무작위로 설정
    private List<GradeSortCombinationDto> groupAndShuffleByGrade(List<GradeSortCombinationDto> filteredGradeCombinations) {
        Map<Double, List<GradeSortCombinationDto>> groupedByGrade = filteredGradeCombinations.stream()
                .collect(Collectors.groupingBy(GradeSortCombinationDto::getTotalGrade, LinkedHashMap::new, Collectors.toList()));
        groupedByGrade.forEach((totalGrade, group) -> Collections.shuffle(group));

        return groupedByGrade.values().stream()
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
                    double totalDistance = distanceCalculator.calculateTotalDistance(
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
        DistanceCalculator distanceCalculator = new DistanceCalculator();
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
                double totalDistance = distanceCalculator.calculateTotalDistance(
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
        DistanceCalculator distanceCalculator = new DistanceCalculator();
        List<DistanceSortCombinationDto> combinations = new ArrayList<>();
        for (StoreForDistanceSortDto firstStore : firstStores) {
            DistanceSortCombinationDto distanceSortCombinationDto = new DistanceSortCombinationDto(firstStore);
            double totalDistance = distanceCalculator.calculateTotalDistance(
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
                        CoordinateDto pass1 = combination.getFirstStore().getCoordinate();
                        CoordinateDto pass2 = combination.getSecondStore().getCoordinate();
                        CoordinateDto endCoordinate = combination.getThirdStore().getCoordinate();

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
                        CoordinateDto pass = combination.getFirstStore().getCoordinate();
                        CoordinateDto endCoordinate = combination.getSecondStore().getCoordinate();

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
                    if (totalRounds == SearchService.ONE_ROUND) {
                        CoordinateDto endCoordinate = combination.getFirstStore().getCoordinate();

                        return new DistanceSortCombinationDto(
                                combination.getFirstStore(),
                                tMapService.fetchFullPathWalkRouteInfo(startCoordinate, endCoordinate)
                        );
                    }
                    throw new InvalidRoundException();
                }).toList();

        return combinations.stream()
                .sorted(Comparator.comparingInt(combination -> combination.getWalkRouteInfoDto().getTotalDistance()))
                .toList();
    }

    private boolean hasDuplicateStores(
            StoreForDistanceSortDto firstStore,
            StoreForDistanceSortDto secondStore,
            StoreForDistanceSortDto thirdStore
    ) {
        Set<Long> storeIds = new HashSet<>();
        storeIds.add(firstStore.getId());
        storeIds.add(secondStore.getId());
        storeIds.add(thirdStore.getId());

        return storeIds.size() != SearchService.THREE_ROUND;
    }

    private boolean hasDuplicateStores(StoreForDistanceSortDto firstStore, StoreForDistanceSortDto secondStore) {
        Set<Long> storeIds = new HashSet<>();
        storeIds.add(firstStore.getId());
        storeIds.add(secondStore.getId());

        return storeIds.size() != SearchService.TWO_ROUND;
    }

    private boolean hasDuplicateStores(
            StoreForGradeSortDto firstStore,
            StoreForGradeSortDto secondStore,
            StoreForGradeSortDto thirdStore
    ) {
        Set<Long> storeIds = new HashSet<>();
        storeIds.add(firstStore.getId());
        storeIds.add(secondStore.getId());
        storeIds.add(thirdStore.getId());

        return storeIds.size() != SearchService.THREE_ROUND;
    }

    private boolean hasDuplicateStores(StoreForGradeSortDto firstStore, StoreForGradeSortDto secondStore) {
        Set<Long> storeIds = new HashSet<>();
        storeIds.add(firstStore.getId());
        storeIds.add(secondStore.getId());

        return storeIds.size() != SearchService.TWO_ROUND;
    }
}
