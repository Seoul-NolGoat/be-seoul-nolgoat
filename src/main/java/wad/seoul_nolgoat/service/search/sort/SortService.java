package wad.seoul_nolgoat.service.search.sort;

import org.springframework.stereotype.Service;
import wad.seoul_nolgoat.domain.store.Store;
import wad.seoul_nolgoat.utilty.DistanceCalculator;
import wad.seoul_nolgoat.web.search.dto.response.CombinationDto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

@Service
public class SortService {

    public static final String KAKAO = "kakao";
    public static final String NOLGOAT = "nolgoat";

    public static final int FIRST_RANK = 0;
    public static final int TENTH_RANK = 10;
    public static final int TOP_FIVE = 5;

    public List<CombinationDto> sortByKakaoGrade(
            List<Store> firstStores,
            List<Store> secondStores,
            List<Store> thirdStores) {
        List<Store> sortedFirstStoresByKakaoGrade = sortByGrade(firstStores, KAKAO);
        List<Store> sortedSecondStoresByKakaoGrade = sortByGrade(secondStores, KAKAO);
        List<Store> sortedThirdStoresByKakaoGrade = sortByGrade(thirdStores, KAKAO);

        List<CombinationDto> combinations = generateTopGradeCombinations(
                sortedFirstStoresByKakaoGrade,
                sortedSecondStoresByKakaoGrade,
                sortedThirdStoresByKakaoGrade);

        return Collections.unmodifiableList(sortCombinationsByGrade(combinations, KAKAO)
                .subList(FIRST_RANK, TENTH_RANK));
    }

    public List<CombinationDto> sortByNolgoatGrade(
            List<Store> firstStores,
            List<Store> secondStores,
            List<Store> thirdStores) {
        List<Store> sortedFirstStoresByNolgoatGrade = sortByGrade(firstStores, NOLGOAT);
        List<Store> sortedSecondStoresByNolgoatGrade = sortByGrade(secondStores, NOLGOAT);
        List<Store> sortedThirdStoresByNolgoatGrade = sortByGrade(thirdStores, NOLGOAT);

        List<CombinationDto> combinations = generateTopGradeCombinations(
                sortedFirstStoresByNolgoatGrade,
                sortedSecondStoresByNolgoatGrade,
                sortedThirdStoresByNolgoatGrade);

        return Collections.unmodifiableList(sortCombinationsByGrade(combinations, NOLGOAT).
                subList(FIRST_RANK, TENTH_RANK));
    }

    //sortByDistance는 인자 하나 더 추가해야함(현재 위치)

    public List<CombinationDto> sortByDistance(
            List<Store> firstStores,
            List<Store> secondStores,
            List<Store> thirdStores) {
        List<CombinationForDistance> combinationForDistances = generateDistanceCombinations(
                firstStores,
                secondStores,
                thirdStores);

        return combinationForDistances.stream()
                .sorted(Comparator.comparingDouble(CombinationForDistance::getTotalDistance))
                .limit(20)
                .map(i -> new CombinationDto(i.getStores()))
                .toList();
    }

    private List<Store> sortByGrade(List<Store> stores, String keyword) {
        if (keyword.equals(KAKAO)) {
            stores.sort(Comparator.comparingDouble(Store::getKakaoAverageGrade).reversed());
        } else {
            stores.sort(Comparator.comparingDouble(Store::getNolgoatAverageGrade).reversed());
        }

        return stores;
    }

    private List<CombinationDto> sortCombinationsByGrade(List<CombinationDto> stores, String keyword) {
        stores.sort((a, b) -> {

            double firstRate = a.getStores().stream()
                    .mapToDouble(store -> keyword.equals(KAKAO)
                            ? store.getKakaoAverageGrade()
                            : store.getNolgoatAverageGrade())
                    .sum();

            double secondRate = b.getStores().stream()
                    .mapToDouble(store -> keyword.equals(KAKAO)
                            ? store.getKakaoAverageGrade()
                            : store.getNolgoatAverageGrade())
                    .sum();

            return Double.compare(secondRate, firstRate);
        });

        return stores;
    }

    private List<CombinationDto> generateTopGradeCombinations(
            List<Store> firstStores,
            List<Store> secondStores,
            List<Store> thirdStores) {
        return IntStream.range(0, TOP_FIVE)
                .boxed()
                .flatMap(i -> IntStream.range(0, TOP_FIVE)
                        .boxed()
                        .flatMap(j -> IntStream.range(0, TOP_FIVE)
                                .boxed()
                                .map(k -> new CombinationDto(
                                        List.of(firstStores.get(i), secondStores.get(j), thirdStores.get(k))))))
                .toList();
    }

    private List<CombinationForDistance> generateDistanceCombinations(
            List<Store> firstStores,
            List<Store> secondStores,
            List<Store> thirdStores) {
        DistanceCalculator distanceCalculator = new DistanceCalculator();
        List<CombinationForDistance> combinations = new ArrayList<>();

        for (Store firstStore : firstStores) {
            for (Store secondStore : secondStores) {
                for (Store thirdStore : thirdStores) {

                    CombinationForDistance combinationDto = new CombinationForDistance(List.of(firstStore, secondStore, thirdStore));
                    double totalDistance = distanceCalculator.calculateDistance(combinationDto);
                    combinationDto.setTotalDistance(totalDistance);
                    combinations.add(combinationDto);
                }
            }
        }

        return combinations;
    }

    //    public List<CombinationDto> combineForDistance(
//            List<Store> firstStores,
//            List<Store> secondStores,
//            List<Store> thirdStores) {
//
//        DistanceCalculator distanceCalculator = new DistanceCalculator();
//        ConcurrentLinkedQueue<CombinationDto> combinations = new ConcurrentLinkedQueue<>();
//        int firstSize = firstStores.size();
//        int secondSize = secondStores.size();
//        int thirdSize = thirdStores.size();
//
//        IntStream.range(0, firstSize).parallel().forEach(i -> {
//            Store firstStore = firstStores.get(i);
//            IntStream.range(0, secondSize).parallel().forEach(j -> {
//                Store secondStore = secondStores.get(j);
//                IntStream.range(0, thirdSize).parallel().forEach(k -> {
//                    Store thirdStore = thirdStores.get(k);
//                    CombinationDto combinationDto = new CombinationDto(List.of(firstStore, secondStore, thirdStore));
//                    double totalDistance = distanceCalculator.calculateDistance(combinationDto);
//                    combinationDto.setDistance(totalDistance);
//                    combinations.add(combinationDto);
//                });
//            });
//        });
//
//        return new ArrayList<>(combinations);
//    }


//    public List<Store> selectionSort(List<Store> stores) {
//
//        int size = stores.size();
//
//        for (int i = 0; i < size - 1; i++) {
//            int minIndex = i;
//
//            for (int j = i + 1; j < size; j++) {
//                if (stores.get(j).getRate() > stores.get(minIndex).getRate()) {
//                    minIndex = j;
//                }
//            }
//            swap(stores, minIndex, i);
//        }
//        return stores;
//    }


//    public List<Store> bubbleSort(List<Store> list) {
//
//        int size = list.size();
//
//        for (int i = 0; i < size - 1; i++) {
//            for (int j = 0; j < size - i - 1; j++) {
//                if (list.get(j).getRate() < list.get(j + 1).getRate()) {
//                    // Swap list[j] and list[j+1]
//                    Store temp = list.get(j);
//                    list.set(j, list.get(j + 1));
//                    list.set(j + 1, temp);
//                }
//            }
//        }
//        return list;
//    }

//    private void swap(List<Store> list, int a, int b) {
//        Store tmp = list.get(a);
//        list.set(a, list.get(b));
//        list.set(b, tmp);
//    }

}
