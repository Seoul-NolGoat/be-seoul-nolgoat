package wad.seoul_nolgoat.exception;

public class ErrorMessages {

    public static final String USER_NOT_FOUND_MESSAGE = "존재하지 않는 유저입니다.";
    public static final String STORE_NOT_FOUND_MESSAGE = "존재하지 않는 가게입니다.";
    public static final String REVIEW_NOT_FOUND_MESSAGE = "존재하지 않는 리뷰입니다.";
    public static final String INVALID_GATHERING_ROUND = "유효하지 않은 회식 차수입니다.";
    public static final String INVALID_SEARCH_CRITERIA = "유효하지 않은 검색 기준입니다.";
    public static final String TOKEN_EXPIRED_MESSAGE = "기한이 만료된 토큰입니다.";
    public static final String UNSUPPORTED_PROVIDER_MESSAGE = "지원하지 않는 제공자입니다.";
    public static final String ADDRESS_CONVERSION_FAILED_MESSAGE = "주소로 변환할 수 없습니다.";
    public static final String WALKING_DISTANCE_CALCULATION_FAILED_MESSAGE = "도보 거리를 계산할 수 없습니다.";
    public static final String TMAP_API_CALL_FAILED_MESSAGE = "TMap API 호출에 실패했습니다.";

    private ErrorMessages() {
    }
}
