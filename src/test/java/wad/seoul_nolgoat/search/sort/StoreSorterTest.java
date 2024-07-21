package wad.seoul_nolgoat.search.sort;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.FieldReflectionArbitraryIntrospector;
import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import wad.seoul_nolgoat.domain.store.Store;
import wad.seoul_nolgoat.domain.store.StoreRepository;
import wad.seoul_nolgoat.service.search.sort.SortService;
import wad.seoul_nolgoat.web.search.dto.response.CombinationDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static wad.seoul_nolgoat.service.search.sort.SortService.KAKAO;
import static wad.seoul_nolgoat.service.search.sort.SortService.NOLGOAT;

@SpringBootTest
class StoreSorterTest {

    @Autowired
    StoreRepository storeRepository;

    @Autowired
    SortService sortService;

    @DisplayName("카카오 평점을 기준으로 내림차순 정렬한다.")
    @Test
    void 카카오_평점_정렬_테스트() {

        List<Store> firstStores = generateStores();
        List<Store> secondStores = generateStores();
        List<Store> thridStores = generateStores();

        List<CombinationDto> combinations = sortService.sortByKakaoGrade(firstStores, secondStores, thridStores);
        for (int i = 0; i < combinations.size() - 1; i++) {
            assertThat(plusRates(combinations.get(i), KAKAO) >= plusRates(combinations.get(i + 1), KAKAO)).isTrue();
        }
    }

    @DisplayName("카카오 평점을 기준으로 내림차순했을 때, A1보다 A2가 값이 더 크다고 하면 테스트가 실패한다.")
    @Test
    void 카카오_평점_정렬_테스트_실패() {


        List<Store> firstStores = generateStores();
        List<Store> secondStores = generateStores();
        List<Store> thridStores = generateStores();

        List<CombinationDto> combinations = sortService.sortByKakaoGrade(firstStores, secondStores, thridStores);
        for (int i = 0; i < combinations.size() - 1; i++) {
            assertThat(plusRates(combinations.get(i), KAKAO) < plusRates(combinations.get(i + 1), KAKAO)).isFalse();
        }
    }


    @DisplayName("놀곳 평점을 기준으로 내림차순 정렬한다.")
    @Test
    void 놀곳_평점_정렬_테스트() {

        List<Store> firstStores = generateStores();
        List<Store> secondStores = generateStores();
        List<Store> thirdStores = generateStores();

        List<CombinationDto> combinations = sortService.sortByNolgoatGrade(firstStores, secondStores, thirdStores);
        for (int i = 0; i < combinations.size() - 1; i++) {
            assertThat(plusRates(combinations.get(i), NOLGOAT) >= plusRates(combinations.get(i + 1), NOLGOAT)).isTrue();
        }
    }

    @DisplayName("놀곳 평점을 기준으로 내림차순했을 때, A1보다 A2가 값이 더 크다고 하면 테스트가 실패한다.")
    @Test
    void 놀곳_평점_정렬_테스트_실패() {

        List<Store> firstStores = generateStores();
        List<Store> secondStores = generateStores();
        List<Store> thirdStores = generateStores();

        List<CombinationDto> combinations = sortService.sortByNolgoatGrade(firstStores, secondStores, thirdStores);
        for (int i = 0; i < combinations.size() - 1; i++) {
            assertThat(plusRates(combinations.get(i), NOLGOAT) < plusRates(combinations.get(i + 1), NOLGOAT)).isFalse();
        }
    }

    @Test
    void test() {

        List<Long> firstIds = new ArrayList<>();
        Random random = new Random();

        // 랜덤 ID 생성
        for (int i = 0; i < 100; i++) {
            long randomId = 3400L + random.nextInt(120000 - 3400 + 1);
            firstIds.add(randomId);
        }

        List<Long> secondIds = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            long randomId = 3400L + random.nextInt(120000 - 3400 + 1);
            secondIds.add(randomId);
        }

        List<Long> thirdIds = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            long randomId = 3400L + random.nextInt(120000 - 3400 + 1);
            thirdIds.add(randomId);
        }

        List<Store> firstStores = storeRepository.findAllById(firstIds);
        List<Store> secondStores = storeRepository.findAllById(secondIds);
        List<Store> thirdStores = storeRepository.findAllById(thirdIds);

        List<CombinationDto> combinations = sortService.sortByKakaoGrade(firstStores, secondStores, thirdStores);

        for (int i = 0; i < combinations.size() - 1; i++) {
            assertThat(plusRates(combinations.get(i), KAKAO) >= plusRates(combinations.get(i + 1), KAKAO)).isTrue();
        }
    }

    @Test
    void test2() {

        List<Long> firstIds = new ArrayList<>();

        Random random = new Random();

        for (int i = 0; i < 100; i++) {
            long randomId = 3400L + random.nextInt(120000 - 3400 + 1);
            firstIds.add(randomId);
        }

        List<Long> secondIds = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            long randomId = 3400L + random.nextInt(120000 - 3400 + 1);
            secondIds.add(randomId);
        }

        List<Long> thirdIds = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            long randomId = 3400L + random.nextInt(120000 - 3400 + 1);
            thirdIds.add(randomId);
        }

        List<Store> firstStores = storeRepository.findAllById(firstIds);
        List<Store> secondStores = storeRepository.findAllById(secondIds);
        List<Store> thirdStores = storeRepository.findAllById(thirdIds);

        List<CombinationDto> combinations = sortService.sortByKakaoGrade(firstStores, secondStores, thirdStores);

        for (int i = 0; i < combinations.size() - 1; i++) {
            assertThat(plusRates(combinations.get(i), KAKAO) < plusRates(combinations.get(i + 1), KAKAO)).isFalse();
        }
    }
//
//    @Test
//    void 조합_생성_테스트() {
//
//        StoreSorter storeSorter = new StoreSorter();
//        List<Store> firstStores = generateStores();
//        List<Store> secondStores = generateStores();
//        List<Store> thirdStores = generateStores();
//
//        int numberOfRuns = 10;
//        long totalDuration = 0;
//
//        for (int i = 0; i < numberOfRuns; i++) {
//
//            long startTime = System.nanoTime();
//            storeSorter.sortByDistance(firstStores, secondStores, thirdStores);
//            long endTime = System.nanoTime();
//            long duration = endTime - startTime;
//            totalDuration += duration;
//
//        }
//        long averageDuration = totalDuration / numberOfRuns;
//        System.out.println("Average test execution time: " + averageDuration + " nanoseconds");
//    }


//    @Test
//    void 조합_생성_테스트_내림차순() {
//
//        StoreSorter storeSorter = new StoreSorter();
//        List<Store> firstStores = generateStores();
//        List<Store> secondStores = generateStores();
//        List<Store> thirdStores = generateStores();
//
//        List<CombinationDto> combinationDtos = storeSorter.sortByDistance(firstStores, secondStores, thirdStores);
//
//        for (int i = 0; i < combinationDtos.size() - 1; i++) {
//            assertThat(
//                    combinationDtos.get(i).getTotalDistance() <= combinationDtos.get(i + 1).getTotalDistance())
//                    .isTrue();
//        }
//    }

//    @Test
//    void 조합_생성_테스트_내림차순_실패() {
//
//        StoreSorter storeSorter = new StoreSorter();
//        List<Store> firstStores = generateStores();
//        List<Store> secondStores = generateStores();
//        List<Store> thirdStores = generateStores();
//
//        List<CombinationDto> combinationDtos = storeSorter.sortByDistance(firstStores, secondStores, thirdStores);
//
//        for (int i = 0; i < combinationDtos.size() - 1; i++) {
//            assertThat(
//                    combinationDtos.get(i).getTotalDistance() > combinationDtos.get(i + 1).getTotalDistance())
//                    .isFalse();
//        }
//    }

//    @Test
//    void test(){
//
//        ArrayList<Long> id = new ArrayList<>();
//        for(Long i =6000L; i<6200L; i++){
//            id.add(i);
//        }
//
//        StoreSorter storeSorter = new StoreSorter();
//
//        List<Store> storeList = storeRepository.findAllById(id);
//
//        long startTime = System.nanoTime();
////        List<Store> stores = storeSorter.bubbleSort(storeList);
//        List<Store> stores = storeSorter.selectionSort(storeList);
//
//        long endTime = System.nanoTime();
//        long duration = endTime - startTime;
//        System.out.println("Test execution time: " + duration + " nanoseconds");
//    }
//
//    @Test
//    void 랜덤_id_생성(){
//
//
//        ArrayList<Long> id = new ArrayList<>();
//
//        Random random = new Random();
//
//        // 랜덤 ID 생성
//        for (int i = 0; i < 200; i++) {
//            long randomId = 3400L + random.nextInt(120000 - 3400 + 1);
//            id.add(randomId);
//        }
//
//        StoreSorter storeSorter = new StoreSorter();
//
//        List<Store> storeList = storeRepository.findAllById(id);
//
//        long startTime = System.nanoTime();
//        List<Store> stores = storeSorter.bubbleSort(storeList);
//
//        long endTime = System.nanoTime();
//        long duration = endTime - startTime;
//        System.out.println("Test execution time: " + duration + " nanoseconds");
//    }
//
//    @Test
//    void 평균_시간_tim_sort(){
//            int numberOfRuns = 10;
//            long totalDuration = 0;
//
//            for (int run = 0; run < numberOfRuns; run++) {
//                ArrayList<Long> id = new ArrayList<>();
//                Random random = new Random();
//
//                // 랜덤 ID 생성
//                for (int i = 0; i < 100000; i++) {
//                    long randomId = 3400L + random.nextInt(120000 - 3400 + 1);
//                    id.add(randomId);
//                }
//
//                StoreSorter storeSorter = new StoreSorter();
//                List<Store> storeList = storeRepository.findAllById(id);
//
//                long startTime = System.nanoTime();
//                List<Store> stores = storeSorter.sortByKakaoRate(storeList);
//                long endTime = System.nanoTime();
//
//                long duration = endTime - startTime;
//                totalDuration += duration;
//
//            }
//
//            long averageDuration = totalDuration / numberOfRuns;
//            System.out.println("Average test execution time: " + averageDuration + " nanoseconds");
//        }
//
//    @Test
//    void test2(){
//
//        ArrayList<Long> id = new ArrayList<>();
//
//        Random random = new Random();
//
//        // 랜덤 ID 생성
//        for (int i = 0; i < 200; i++) {
//            long randomId = 3400L + random.nextInt(120000 - 3400 + 1);
//            id.add(randomId);
//        }
//
//        StoreSorter storeSorter = new StoreSorter();
//
//        List<Store> storeList = storeRepository.findAllById(id);
//
////        List<Store> stores = storeSorter.bubbleSort(storeList);
//        List<Store> stores = storeSorter.selectionSort(storeList);
//
//        for(int i =0; i<stores.size()-1; i++) {
//            assertThat(stores.get(i).getRate() >= stores.get(i+1).getRate()).isTrue();
//        }
//    }
//
//    @DisplayName("내림차순으로 정렬했을 때 o1의 평점이 o2의 평점보다 낮은지를 확인하면 테스트가 실패한다.")
//    @Test
//    void test3(){
//
//        ArrayList<Long> id = new ArrayList<>();
//
//        Random random = new Random();
//
//        // 랜덤 ID 생성
//        for (int i = 0; i < 200; i++) {
//            long randomId = 3400L + random.nextInt(120000 - 3400 + 1);
//            id.add(randomId);
//        }
//
//        StoreSorter storeSorter = new StoreSorter();
//
//        List<Store> storeList = storeRepository.findAllById(id);
////        List<Store> stores = storeSorter.bubbleSort(storeList);
//        List<Store> stores = storeSorter.selectionSort(storeList);
//
//        for(int i =0; i<stores.size()-1; i++) {
//            assertThat(stores.get(i).getRate() < stores.get(i+1).getRate()).isFalse();
//        }
//    }


    private List<Store> generateStores() {

        FixtureMonkey fixtureMonkey = FixtureMonkey.builder()
                .objectIntrospector(FieldReflectionArbitraryIntrospector.INSTANCE)
                .build();

        Arbitrary<String> koreanSetting = Arbitraries
                .strings()
                .withChars("가나다라마바사아자차카타파하")
                .ofMinLength(2)
                .ofMaxLength(10);

        Arbitrary<Double> gradeSetting = Arbitraries
                .doubles()
                .between(0.0, 5.0);

        Arbitrary<Double> latitudeSetting = Arbitraries
                .doubles()
                .between(33.0, 38.6);

        Arbitrary<Double> longitudeSetting = Arbitraries
                .doubles()
                .between(124.0, 131.0);

        return fixtureMonkey.giveMeBuilder(Store.class)
                .set("name", koreanSetting)
                .set("category", koreanSetting)
                .set("lotAddress", koreanSetting)
                .set("roadAddress", koreanSetting)
                .set("kakaoAverageGrade", gradeSetting)
                .set("nolgoatAverageGrade", gradeSetting)
                .set("latitude", latitudeSetting)
                .set("longitude", longitudeSetting)
                .sampleList(100);
    }

    private double plusRates(CombinationDto combinationDto, String keyword) {
        List<Store> stores = combinationDto.getStores();

        double sum = stores.stream().mapToDouble(i -> keyword.equals(KAKAO) ?
                i.getKakaoAverageGrade() :
                i.getNolgoatAverageGrade()).sum();

        return sum;

    }
}