package wad.seoul_nolgoat.service.search.sort;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import wad.seoul_nolgoat.service.search.dto.*;
import wad.seoul_nolgoat.service.tMap.TMapService;
import wad.seoul_nolgoat.service.tMap.dto.WalkRouteInfoDto;
import wad.seoul_nolgoat.web.search.dto.CoordinateDto;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
@Service
public class SortService {

    private static final int TOP_FIRST = 0;
    private static final int TOP_TWENTIETH = 20;

    private final TMapService tMapService;

    public List<GradeSortCombinationDto> sortStoresByGrade(SortConditionDto<StoreForGradeSortDto> sortConditionDto) {
        int totalRounds = sortConditionDto.getTotalRounds();
        List<GradeSortCombinationDto> gradeCombinations = generateGradeCombinations(sortConditionDto, totalRounds);

        return sortCombinationsByGrade(gradeCombinations);
    }

    public List<DistanceSortCombinationDto> sortStoresByDistance(SortConditionDto<StoreForDistanceSortDto> sortConditionDto) {
        int totalRounds = sortConditionDto.getTotalRounds();
        List<DistanceSortCombinationDto> distanceCombinations = generateAndSortDistanceCombinations(
                sortConditionDto,
                totalRounds
        ).subList(TOP_FIRST, TOP_TWENTIETH);

        return fetchDistancesFromTMapApi(distanceCombinations, totalRounds);
    }

    // 테스트를 위해 접근제어자를 public으로 변경
    public List<DistanceSortCombinationDto> generateAndSortDistanceCombinations(
            SortConditionDto<StoreForDistanceSortDto> sortConditionDto,
            int totalRounds) {
        if (totalRounds == 3) {
            return createDistanceCombinationsForThreeRounds(
                    sortConditionDto.getFirstFilteredStores(),
                    sortConditionDto.getSecondFilteredStores(),
                    sortConditionDto.getThirdFilteredStores(),
                    sortConditionDto.getStartCoordinate()
            ).stream()
                    .sorted(Comparator.comparingDouble(DistanceSortCombinationDto::getTotalDistnace))
                    .toList();
        }
        if (totalRounds == 2) {
            return createDistanceCombinationsForTwoRounds(
                    sortConditionDto.getFirstFilteredStores(),
                    sortConditionDto.getSecondFilteredStores(),
                    sortConditionDto.getStartCoordinate()
            ).stream()
                    .sorted(Comparator.comparingDouble(DistanceSortCombinationDto::getTotalDistnace))
                    .toList();
        }
        if (totalRounds == 1) {
            return createDistanceCombinationsForOneRound(
                    sortConditionDto.getFirstFilteredStores(),
                    sortConditionDto.getStartCoordinate()
            ).stream()
                    .sorted(Comparator.comparingDouble(DistanceSortCombinationDto::getTotalDistnace))
                    .toList();
        }
        throw new RuntimeException();
    }

    private List<GradeSortCombinationDto> generateGradeCombinations(
            SortConditionDto<StoreForGradeSortDto> sortConditionDto,
            int totalRounds) {
        if (totalRounds == 3) {
            return createGradeCombinationsForThreeRounds(
                    sortConditionDto.getFirstFilteredStores(),
                    sortConditionDto.getSecondFilteredStores(),
                    sortConditionDto.getThirdFilteredStores()
            );
        }
        if (totalRounds == 2) {
            return createGradeCombinationsForTwoRounds(
                    sortConditionDto.getFirstFilteredStores(),
                    sortConditionDto.getSecondFilteredStores()
            );
        }
        if (totalRounds == 1) {
            return createGradeCombinationsForOneRound(sortConditionDto.getFirstFilteredStores());
        }
        throw new RuntimeException();
    }

    private List<GradeSortCombinationDto> createGradeCombinationsForThreeRounds(
            List<StoreForGradeSortDto> firstStores,
            List<StoreForGradeSortDto> secondStores,
            List<StoreForGradeSortDto> thirdStores) {
        List<GradeSortCombinationDto> gradeCombinations = new ArrayList<>();
        for (StoreForGradeSortDto firstStore : firstStores) {
            for (StoreForGradeSortDto secondStore : secondStores) {
                for (StoreForGradeSortDto thirdStore : thirdStores) {
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
            List<StoreForGradeSortDto> secondStores) {
        List<GradeSortCombinationDto> gradeCombinations = new ArrayList<>();
        for (StoreForGradeSortDto firstStore : firstStores) {
            for (StoreForGradeSortDto secondStore : secondStores) {
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

    private List<GradeSortCombinationDto> sortCombinationsByGrade(List<GradeSortCombinationDto> combinations) {
        combinations.sort((combination1, combination2) -> {
            double firstRate = combination1.getFirstStore().getAverageGrade()
                    + combination1.getSecondStore().getAverageGrade()
                    + combination1.getThirdStore().getAverageGrade();
            double secondRate = combination2.getFirstStore().getAverageGrade()
                    + combination2.getSecondStore().getAverageGrade()
                    + combination2.getThirdStore().getAverageGrade();

            return Double.compare(secondRate, firstRate);
        });

        return combinations;
    }

    private List<DistanceSortCombinationDto> createDistanceCombinationsForThreeRounds(
            List<StoreForDistanceSortDto> firstStores,
            List<StoreForDistanceSortDto> secondStores,
            List<StoreForDistanceSortDto> thirdStores,
            CoordinateDto coordinateDto) {
        DistanceCalculator distanceCalculator = new DistanceCalculator(); // Bean으로 등록 예정
        List<DistanceSortCombinationDto> combinations = new ArrayList<>();
        for (StoreForDistanceSortDto firstStore : firstStores) {
            for (StoreForDistanceSortDto secondStore : secondStores) {
                for (StoreForDistanceSortDto thirdStore : thirdStores) {
                    DistanceSortCombinationDto distanceSortCombinationDto = new DistanceSortCombinationDto(
                            firstStore,
                            secondStore,
                            thirdStore
                    );
                    double totalDistance = distanceCalculator.calculateDistance(
                            distanceSortCombinationDto,
                            coordinateDto
                    );
                    distanceSortCombinationDto.setTotalDistnace(totalDistance);
                    combinations.add(distanceSortCombinationDto);
                }
            }
        }

        return combinations;
    }

    private List<DistanceSortCombinationDto> createDistanceCombinationsForTwoRounds(
            List<StoreForDistanceSortDto> firstStores,
            List<StoreForDistanceSortDto> secondStores,
            CoordinateDto coordinateDto) {
        DistanceCalculator distanceCalculator = new DistanceCalculator();
        List<DistanceSortCombinationDto> combinations = new ArrayList<>();
        for (StoreForDistanceSortDto firstStore : firstStores) {
            for (StoreForDistanceSortDto secondStore : secondStores) {
                DistanceSortCombinationDto distanceSortCombinationDto = new DistanceSortCombinationDto(
                        firstStore,
                        secondStore
                );
                double totalDistance = distanceCalculator.calculateDistance(distanceSortCombinationDto, coordinateDto);
                distanceSortCombinationDto.setTotalDistnace(totalDistance);
                combinations.add(distanceSortCombinationDto);
            }
        }

        return combinations;
    }

    private List<DistanceSortCombinationDto> createDistanceCombinationsForOneRound(
            List<StoreForDistanceSortDto> firstStores,
            CoordinateDto coordinateDto) {
        DistanceCalculator distanceCalculator = new DistanceCalculator();
        List<DistanceSortCombinationDto> combinations = new ArrayList<>();
        for (StoreForDistanceSortDto firstStore : firstStores) {
            DistanceSortCombinationDto distanceSortCombinationDto = new DistanceSortCombinationDto(firstStore);
            double totalDistance = distanceCalculator.calculateDistance(distanceSortCombinationDto, coordinateDto);
            distanceSortCombinationDto.setTotalDistnace(totalDistance);
            combinations.add(distanceSortCombinationDto);
        }

        return combinations;
    }

    private List<DistanceSortCombinationDto> fetchDistancesFromTMapApi(
            List<DistanceSortCombinationDto> distanceSortCombinationDtos,
            int totalRounds) {
        List<DistanceSortCombinationDto> combinations = distanceSortCombinationDtos.parallelStream()
                .map(combination -> {
                            if (totalRounds == 3) {
                                CoordinateDto startCoordinate = combination.getFirstStore().getCoordinate();
                                CoordinateDto pass = combination.getSecondStore().getCoordinate();
                                CoordinateDto endCoordinate = combination.getThirdStore().getCoordinate();
                                WalkRouteInfoDto walkRouteInfoDto = tMapService.fetchWalkRouteInfo(
                                        startCoordinate,
                                        pass,
                                        endCoordinate
                                );

                                return new DistanceSortCombinationDto(
                                        combination.getFirstStore(),
                                        combination.getSecondStore(),
                                        combination.getThirdStore(),
                                        walkRouteInfoDto
                                );
                            }
                            if (totalRounds == 2) {
                                CoordinateDto startCoordinate = combination.getFirstStore().getCoordinate();
                                CoordinateDto endCoordinate = combination.getSecondStore().getCoordinate();
                                WalkRouteInfoDto walkRouteInfoDto = tMapService.fetchWalkRouteInfo(
                                        startCoordinate,
                                        endCoordinate
                                );

                                return new DistanceSortCombinationDto(
                                        combination.getFirstStore(),
                                        combination.getSecondStore(),
                                        combination.getThirdStore(),
                                        walkRouteInfoDto
                                );
                            }
                            if (totalRounds == 1) {
                                CoordinateDto startCoordinate = combination.getFirstStore().getCoordinate();
                                CoordinateDto endCoordinate = combination.getSecondStore().getCoordinate();
                                WalkRouteInfoDto walkRouteInfoDto = tMapService.fetchWalkRouteInfo(
                                        startCoordinate,
                                        endCoordinate
                                );

                                return new DistanceSortCombinationDto(
                                        combination.getFirstStore(),
                                        combination.getSecondStore(),
                                        combination.getThirdStore(),
                                        walkRouteInfoDto
                                );
                            }
                            throw new RuntimeException();
                        }
                ).toList();

        return combinations.stream()
                .sorted(Comparator.comparingInt(combination -> combination.getWalkRouteInfoDto().getTotalDistance()))
                .toList();
    }
}
