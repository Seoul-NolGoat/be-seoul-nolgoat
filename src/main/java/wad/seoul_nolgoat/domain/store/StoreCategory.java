package wad.seoul_nolgoat.domain.store;

import wad.seoul_nolgoat.exception.ApiException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static wad.seoul_nolgoat.exception.ErrorCode.CATEGORY_NOT_FOUND;

public enum StoreCategory {

    KOREAN_FOOD("한식"),
    FUSION_KOREAN_FOOD("퓨전한식"),
    GOBCHANG_MAKCHANG("곱창,막창"),
    NOODLES("국수"),
    MEAT("육류,고기", "고기뷔페"),
    CHUO("추어"),
    KALGUKSU("칼국수"),
    COLD_NOODLES("냉면"),
    SEOLLEONGTANG("설렁탕"),
    SAMGYETANG("삼계탕"),
    JOKBAL_BOSSAM("족발,보쌈"),
    SAMGYEOPSAL("삼겹살"),
    JJIGAE("찌개"),
    JEONGOL("전골"),
    BULGOGI("불고기"),
    DURUCHIGI("두루치기"),
    HANJEONGSIK("한정식"),
    JUK("죽"),
    GUKBAP("국밥", "해장국"),
    GOMTANG("곰탕"),
    SSAMBAP("쌈밥"),
    LAMB_SKEWER("양꼬치"),
    CHINESE_FOOD("중국요리"),
    CHINESE_CUISINE("중식"),
    FUSION_CHINESE_FOOD("퓨전중식"),
    JAPANESE_FOOD("일식"),
    TUNA_SASHIMI("참치회"),
    SHABU_SHABU("샤브샤브"),
    TEPPANYAKI("철판요리"),
    IZAKAYA("일본식주점"),
    SUSHI_ROLL("초밥,롤"),
    DONKATSU_UDON("돈까스,우동"),
    JJIGAE_JEONGOL("찌개,전골"),
    JAPANESE_RESTAURANT("일식집"),
    RAMEN("일본식라면"),
    HAMBURGER("햄버거"),
    STEAK("스테이크"),
    RIBS("립"),
    PIZZA("피자"),
    BUNSIK("분식"),
    SUNDAE("순대"),
    TTEOKBOKKI("떡볶이"),
    COCKTAIL_BAR("칵테일바"),
    WINE_BAR("와인바"),
    PUB("술집"),
    HOF("호프"),
    GASTROPUB("요리주점"),
    JAPANESE_IZAKAYA("일본식주점"),
    INDOOR_POJANGMACHA("실내포장마차"),
    SEAFOOD("해산물", "해물,생선"),
    SPICY_FISH_STEW("매운탕"),
    SEAFOOD_STEW("해물탕"),
    OYSTER("굴"),
    ABALONE("전복"),
    SASHIMI("회"),
    PUFFER_FISH("복어"),
    CRAB("게"),
    KING_CRAB("대게"),
    BUFFET("뷔페"),
    FUSION_JAPANESE_FOOD("퓨전일식"),
    VEGETARIAN_BUFFET("채식뷔페"),
    SEAFOOD_BUFFET("해산물뷔페"),
    KOREAN_BUFFET("한식뷔페"),
    MEAT_BUFFET("고기뷔페"),
    SANDWICH("샌드위치"),
    ASIAN_CUISINE("아시아음식"),
    CHICKEN_DISH("닭요리"),
    FRENCH_CUISINE("프랑스음식"),
    SALAD("샐러드"),
    ITALIAN_CUISINE("이탈리안"),
    SPANISH_CUISINE("스페인음식"),
    RICE_BALL("주먹밥"),
    DUCK_DISH("오리"),
    DAKGANGJEONG("닭강정"),
    CHICKEN("치킨"),
    FAST_FOOD("패스트푸드"),
    DRIVERS_DINER("기사식당"),
    TEPPANYAKI_GRILL("철판요리"),
    SNACK("간식"),
    LUNCH_BOX("도시락"),
    FUSION_CUISINE("퓨전요리"),
    FAMILY_RESTAURANT("패밀리레스토랑"),
    CAFE("카페"),
    KARAOKE("노래방"),
    PCROOM("PC방"),
    BILLIARD("당구");

    private final List<String> categories;

    StoreCategory(String... categories) {
        this.categories = Arrays.asList(categories);
    }

    public static List<String> findRelatedCategoryNames(String category) {
        try {
            return StoreCategory.valueOf(category.toUpperCase()).getCategoryNames();
        } catch (IllegalArgumentException e) {
            throw new ApiException(CATEGORY_NOT_FOUND, e);
        }
    }

    public static Optional<String> findPrimaryCategoryName(String categoryName) {
        return Arrays.stream(StoreCategory.values())
                .filter(storeCategory -> storeCategory.categories.contains(categoryName))
                .map(Enum::name)
                .findFirst();
    }

    private List<String> getCategoryNames() {
        return categories;
    }
}
