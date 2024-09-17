package wad.seoul_nolgoat.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 유저입니다."),
    STORE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 가게입니다."),
    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 리뷰입니다."),
    INQUIRY_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 문의사항입니다."),
    INVALID_GATHERING_ROUND(HttpStatus.BAD_REQUEST, "유효하지 않은 회식 차수입니다."),
    INVALID_SEARCH_CRITERIA(HttpStatus.BAD_REQUEST, "유효하지 않은 검색 기준입니다."),
    TOKEN_EXPIRED_MESSAGE(HttpStatus.UNAUTHORIZED, "기한이 만료된 토큰입니다."),
    UNSUPPORTED_PROVIDER(HttpStatus.UNAUTHORIZED, "지원하지 않는 제공자입니다."),
    ADDRESS_CONVERSION_FAILED(HttpStatus.BAD_REQUEST, "주소로 변환할 수 없습니다."),
    WALKING_DISTANCE_CALCULATION_FAILED(HttpStatus.BAD_REQUEST, "도보 거리를 계산할 수 없습니다."),
    TMAP_API_CALL_FAILED(HttpStatus.BAD_REQUEST, "TMap API 호출에 실패했습니다."),
    DUPLICATE_REVIEW(HttpStatus.CONFLICT, "상점에는 한 명당 리뷰를 한 개만 작성할 수 있습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
