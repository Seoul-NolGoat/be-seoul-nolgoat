package wad.seoul_nolgoat.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // 인증 인가 관련
    UNSUPPORTED_PROVIDER(HttpStatus.UNAUTHORIZED, "AUTH001", "지원하지 않는 제공자입니다."),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "AUTH002", "기한이 만료된 토큰입니다."),

    // 유저 관련
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER001", "존재하지 않는 유저입니다."),

    // 가게 관련
    STORE_NOT_FOUND(HttpStatus.NOT_FOUND, "STORE001", "존재하지 않는 가게입니다."),

    // 리뷰 관련
    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "REVIEW001", "존재하지 않는 리뷰입니다."),
    DUPLICATE_REVIEW(HttpStatus.CONFLICT, "REVIEW002", "가게에는 한 명당 리뷰를 한 개만 작성할 수 있습니다."),

    // 문의 사항 관련
    INQUIRY_NOT_FOUND(HttpStatus.NOT_FOUND, "INQUIRY001", "존재하지 않는 문의 사항입니다."),

    // 공지 관련
    NOTICE_NOT_FOUND(HttpStatus.NOT_FOUND, "NOTICE001", "존재하지 않는 공지입니다."),

    // 검색(필터링, 정렬) 관련
    INVALID_SEARCH_CRITERIA(HttpStatus.BAD_REQUEST, "SEARCH001", "유효하지 않은 검색 기준입니다."),
    INVALID_GATHERING_ROUND(HttpStatus.BAD_REQUEST, "SEARCH002", "유효하지 않은 회식 차수입니다."),

    // 지도 관련
    TMAP_API_CALL_FAILED(HttpStatus.BAD_REQUEST, "MAP001", "TMap API 호출에 실패했습니다."),
    ADDRESS_CONVERSION_FAILED(HttpStatus.BAD_REQUEST, "MAP002", "주소로 변환할 수 없습니다."),
    WALKING_DISTANCE_CALCULATION_FAILED(HttpStatus.BAD_REQUEST, "MAP003", "도보 거리를 계산할 수 없습니다."),

    // 유효성 검사 관련
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "VALIDATION001", "입력값이 유효하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
