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

    //api 호출 횟수를 줄이기 위한 테스트 용도
    public List<DistanceSortCombinationDto> generateAndSortDistanceCombinations(
            SortConditionDto<StoreForDistanceSortDto> sortConditionDto,
            int totalRounds) {
        List<DistanceSortCombinationDto> distanceSortCombinations = null;
        if (totalRounds == 3) {
            distanceSortCombinations = createDistanceCombinationsForThreeRounds(
                    sortConditionDto.getFirstFilteredStores(),
                    sortConditionDto.getSecondFilteredStores(),
                    sortConditionDto.getThirdFilteredStores(),
                    sortConditionDto.getStartCoordinate()
            );
        }
        if (totalRounds == 2) {
            distanceSortCombinations = createDistanceCombinationsForTwoRounds(
                    sortConditionDto.getFirstFilteredStores(),
                    sortConditionDto.getSecondFilteredStores(),
                    sortConditionDto.getStartCoordinate()
            );
        }
        if (totalRounds == 1) {
            distanceSortCombinations = createDistanceCombinationsForOneRound(
                    sortConditionDto.getFirstFilteredStores(),
                    sortConditionDto.getStartCoordinate()
            );
        }

        return distanceSortCombinations.stream()
                .sorted(Comparator.comparingDouble(DistanceSortCombinationDto::getTotalDistnace))
                .toList();
    }

    private List<GradeSortCombinationDto> generateGradeCombinations(
            SortConditionDto<StoreForGradeSortDto> sortConditionDto,
            int totalRounds) {
        List<GradeSortCombinationDto> gradeCombinations = null;
        if (totalRounds == 3) {
            gradeCombinations = createGradeCombinationsForThreeRounds(
                    sortConditionDto.getFirstFilteredStores(),
                    sortConditionDto.getSecondFilteredStores(),
                    sortConditionDto.getThirdFilteredStores()
            );
        }
        if (totalRounds == 2) {
            gradeCombinations = createGradeCombinationsForTwoRounds(
                    sortConditionDto.getFirstFilteredStores(),
                    sortConditionDto.getSecondFilteredStores()
            );
        }
        if (totalRounds == 1) {
            gradeCombinations = createGradeCombinationsForOneRound(sortConditionDto.getFirstFilteredStores());
        }

        return gradeCombinations;
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
        combinations.sort((a, b) -> {
            double firstRate = a.getFirstStore().getAverageGrade()
                    + a.getSecondStore().getAverageGrade()
                    + a.getThirdStore().getAverageGrade();
            double secondRate = b.getFirstStore().getAverageGrade()
                    + b.getSecondStore().getAverageGrade()
                    + b.getThirdStore().getAverageGrade();

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
            List<DistanceSortCombinationDto> list,
            int totalRounds) {
        List<DistanceSortCombinationDto> combinations = list.parallelStream()
                .map(i -> {
                            DistanceSortCombinationDto dto = null;
                            if (totalRounds == 3) {
                                CoordinateDto startCoordinate = i.getFirstStore().getCoordinate();
                                CoordinateDto pass = i.getSecondStore().getCoordinate();
                                CoordinateDto endCoordinate = i.getThirdStore().getCoordinate();
                                WalkRouteInfoDto walkRouteInfoDto = tMapService.fetchWalkRouteInfo(
                                        startCoordinate,
                                        pass,
                                        endCoordinate
                                );
                                dto = new DistanceSortCombinationDto(
                                        i.getFirstStore(),
                                        i.getSecondStore(),
                                        i.getThirdStore(),
                                        walkRouteInfoDto
                                );
                            }
                            if (totalRounds == 2) {
                                CoordinateDto startCoordinate = i.getFirstStore().getCoordinate();
                                CoordinateDto endCoordinate = i.getSecondStore().getCoordinate();
                                WalkRouteInfoDto walkRouteInfoDto = tMapService.fetchWalkRouteInfo(
                                        startCoordinate,
                                        endCoordinate
                                );
                                dto = new DistanceSortCombinationDto(
                                        i.getFirstStore(),
                                        i.getSecondStore(),
                                        i.getThirdStore(),
                                        walkRouteInfoDto
                                );
                            }

                            return dto;
                        }
                ).toList();

        return combinations.stream()
                .sorted(Comparator.comparingInt(o -> o.getWalkRouteInfoDto().getTotalDistance()))
                .toList();
    }
}
