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
    INVALID_AUTHORIZATION_HEADER(HttpStatus.UNAUTHORIZED, "AUTH003", "유효하지 않은 Authorization 헤더입니다."),
    INVALID_TOKEN_FORMAT(HttpStatus.UNAUTHORIZED, "AUTH004", "토큰 형식이 올바르지 않습니다."),
    INVALID_TOKEN_TYPE(HttpStatus.UNAUTHORIZED, "AUTH005", "토큰 타입이 일치하지 않습니다."),
    NULL_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH006", "Refresh 토큰이 null입니다."),
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "AUTH007", "저장소에 존재하지 않는 Refresh 토큰입니다."),

    // 유저 관련
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER001", "존재하지 않는 유저입니다."),

    // 가게 관련
    STORE_NOT_FOUND(HttpStatus.NOT_FOUND, "STORE001", "존재하지 않는 가게입니다."),
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "STORE002", "존재하지 않는 카테고리입니다."),

    // 리뷰 관련
    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "REVIEW001", "존재하지 않는 리뷰입니다."),
    DUPLICATE_REVIEW(HttpStatus.CONFLICT, "REVIEW002", "가게에는 한 명당 리뷰를 한 개만 작성할 수 있습니다."),

    // 파티 관련
    PARTY_NOT_FOUND(HttpStatus.NOT_FOUND, "PARTY001", "존재하지 않는 파티입니다."),
    PARTY_CAPACITY_EXCEEDED(HttpStatus.BAD_REQUEST, "PARTY002", "참여 가능 인원수를 초과했습니다."),
    PARTY_CREATOR_CANNOT_JOIN(HttpStatus.BAD_REQUEST, "PARTY003", "본인이 생성한 파티에는 참여 신청을 할 수 없습니다."),
    PARTY_ALREADY_JOINED(HttpStatus.BAD_REQUEST, "PARTY004", "이미 참여 중인 파티입니다."),
    PARTY_ALREADY_CLOSED(HttpStatus.BAD_REQUEST, "PARTY005", "이미 마감된 파티입니다."),
    PARTY_ALREADY_DELETED(HttpStatus.BAD_REQUEST, "PARTY006", "이미 삭제된 파티입니다."),
    PARTY_NOT_HOST(HttpStatus.FORBIDDEN, "PARTY007", "파티 생성자만 참여자를 추방할 수 있습니다."),
    PARTY_USER_NOT_FOUND(HttpStatus.NOT_FOUND, "PARTY008", "해당 파티에 참여하지 않은 유저입니다."),

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
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "VALIDATION001", "입력값이 유효하지 않습니다."),

    // 파일 업로드 관련
    FILE_READ_FAILED(HttpStatus.BAD_REQUEST, "FILE003", "파일을 읽는 데 실패했습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
