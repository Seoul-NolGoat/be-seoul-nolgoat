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
    INVALID_AUTHORIZATION_HEADER(HttpStatus.UNAUTHORIZED, "AUTH003", "올바르지 않은 Authorization 헤더입니다."),
    INVALID_TOKEN_FORMAT(HttpStatus.UNAUTHORIZED, "AUTH004", "토큰 형식이 올바르지 않습니다."),
    INVALID_TOKEN_TYPE(HttpStatus.UNAUTHORIZED, "AUTH005", "토큰 타입이 올바르지 않습니다."),
    INVALID_TOKEN_ISSUER(HttpStatus.UNAUTHORIZED, "AUTH006", "토큰 발급자가 올바르지 않습니다."),
    MISSING_COOKIE(HttpStatus.BAD_REQUEST, "AUTH007", "필수 쿠키가 누락되었습니다."),
    MISSING_HEADER(HttpStatus.BAD_REQUEST, "AUTH008", "필수 헤더가 누락되었습니다."),
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "AUTH009", "저장소에 존재하지 않는 Refresh 토큰입니다."),
    INVALID_CSRF_PROTECTION_UUID(HttpStatus.UNAUTHORIZED, "AUTH010", "유효하지 않은 CSRF Protection UUID입니다."),
    WITHDRAWAL_CODE_EXPIRED(HttpStatus.UNAUTHORIZED, "AUTH011", "회원 탈퇴 인증 시간이 만료되었습니다."),
    INVALID_REQUEST_URI(HttpStatus.NOT_FOUND, "AUTH012", "유효하지 않은 요청 URI입니다."),

    // 유저 관련
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER001", "존재하지 않는 유저입니다."),

    // 가게 관련
    STORE_NOT_FOUND(HttpStatus.NOT_FOUND, "STORE001", "존재하지 않는 가게입니다."),
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "STORE002", "존재하지 않는 카테고리입니다."),

    // 리뷰 관련
    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "REVIEW001", "존재하지 않는 리뷰입니다."),
    DUPLICATE_REVIEW(HttpStatus.CONFLICT, "REVIEW002", "가게에는 한 명당 리뷰를 한 개만 작성할 수 있습니다."),
    REVIEW_WRITER_MISMATCH(HttpStatus.FORBIDDEN, "REVIEW003", "리뷰의 작성자가 아닙니다."),

    // 북마크 관련
    BOOKMARK_NOT_FOUND(HttpStatus.NOT_FOUND, "BOOKMARK001", "존재하지 않는 북마크입니다."),
    BOOKMARK_REGISTRANT_MISMATCH(HttpStatus.FORBIDDEN, "BOOKMARK002", "북마크를 등록한 유저가 아닙니다."),

    // 파티 관련
    PARTY_NOT_FOUND(HttpStatus.NOT_FOUND, "PARTY001", "존재하지 않는 파티입니다."),
    PARTY_CAPACITY_EXCEEDED(HttpStatus.BAD_REQUEST, "PARTY002", "참여 가능 인원수를 초과했습니다."),
    PARTY_CREATOR_CANNOT_JOIN(HttpStatus.BAD_REQUEST, "PARTY003", "본인이 생성한 파티에는 참여 신청을 할 수 없습니다."),
    PARTY_ALREADY_JOINED(HttpStatus.BAD_REQUEST, "PARTY004", "이미 참여 중인 파티입니다."),
    PARTY_ALREADY_CLOSED(HttpStatus.BAD_REQUEST, "PARTY005", "이미 마감된 파티입니다."),
    PARTY_ALREADY_DELETED(HttpStatus.BAD_REQUEST, "PARTY006", "이미 삭제된 파티입니다."),
    PARTY_KICK_NOT_AUTHORIZED(HttpStatus.FORBIDDEN, "PARTY007", "파티 생성자만 참여자를 추방할 수 있습니다."),
    PARTY_CLOSE_NOT_AUTHORIZED(HttpStatus.FORBIDDEN, "PARTY008", "파티 생성자만 파티를 마감할 수 있습니다."),
    PARTY_UPDATE_NOT_AUTHORIZED(HttpStatus.FORBIDDEN, "PARTY009", "파티 생성자만 파티를 수정할 수 있습니다."),
    PARTY_DELETE_NOT_AUTHORIZED(HttpStatus.FORBIDDEN, "PARTY010", "파티 생성자만 파티를 삭제할 수 있습니다."),
    PARTY_USER_NOT_FOUND(HttpStatus.NOT_FOUND, "PARTY011", "해당 파티에 참여하지 않은 유저입니다."),
    INVALID_PARTY_STATUS(HttpStatus.BAD_REQUEST, "PARTY012", "올바르지 않은 파티 상태값입니다."),
    INVALID_ADMINISTRATIVE_DISTRICT(HttpStatus.BAD_REQUEST, "PARTY013", "올바르지 않은 행정구역입니다."),
    INVALID_MAX_CAPACITY(HttpStatus.BAD_REQUEST, "PARTY014", "참여 가능 인원은 현재 인원보다 적을 수 없습니다."),
    PARTY_COUNT_BELOW_MINIMUM(HttpStatus.BAD_REQUEST, "PARTY015", "현재 인원수는 1보다 작을 수 없습니다."),
    PARTY_CREATOR_CANNOT_LEAVE(HttpStatus.BAD_REQUEST, "PARTY016", "본인이 생성한 파티에서 탈퇴 할 수 없습니다."),
    PARTY_CONCURRENT_ERROR(HttpStatus.CONFLICT, "PARTY017", "문제가 발생했습니다. 잠시 후 다시 시도해주세요."),

    // 댓글 관련
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "COMMENT001", "존재하지 않는 댓글입니다."),
    COMMENT_UPDATE_NOT_AUTHORIZED(HttpStatus.FORBIDDEN, "COMMENT002", "댓글 작성자만 댓글을 수정할 수 있습니다."),
    COMMENT_DELETE_NOT_AUTHORIZED(HttpStatus.FORBIDDEN, "COMMENT003", "댓글 작성자만 댓글을 삭제할 수 있습니다."),

    // 건의 관련
    INQUIRY_NOT_FOUND(HttpStatus.NOT_FOUND, "INQUIRY001", "존재하지 않는 건의 사항입니다."),
    INQUIRY_WRITER_MISMATCH(HttpStatus.FORBIDDEN, "INQUIRY002", "건의 사항의 작성자가 아닙니다."),

    // 공지 관련
    NOTICE_NOT_FOUND(HttpStatus.NOT_FOUND, "NOTICE001", "존재하지 않는 공지입니다."),
    NOTICE_WRITER_MISMATCH(HttpStatus.FORBIDDEN, "NOTICE002", "공지의 작성자가 아닙니다."),

    // 검색(필터링, 정렬) 관련
    INVALID_SEARCH_CRITERIA(HttpStatus.BAD_REQUEST, "SEARCH001", "유효하지 않은 검색 기준입니다."),
    INVALID_GATHERING_ROUND(HttpStatus.BAD_REQUEST, "SEARCH002", "유효하지 않은 회식 차수입니다."),

    // KakaoMap 관련
    ADDRESS_CONVERSION_FAILED(HttpStatus.BAD_REQUEST, "KAKAOMAP001", "주소로 변환할 수 없습니다."),

    // TMap 관련
    TMAP_API_CALL_FAILED(HttpStatus.BAD_REQUEST, "TMAP001", "TMap API 호출에 실패했습니다."),
    API_RATE_LIMIT_EXCEEDED(HttpStatus.TOO_MANY_REQUESTS, "TMAP002", "TMap API 초당 호출 건수를 초과했습니다."),
    API_QUOTA_EXCEEDED(HttpStatus.TOO_MANY_REQUESTS, "TMAP003", "TMap API 사용 할당량(SLA)을 초과했습니다."),
    WALKING_DISTANCE_CALCULATION_FAILED(HttpStatus.BAD_REQUEST, "TMAP004", "도보 거리를 계산할 수 없습니다."),

    // 유효성 검사 관련
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "VALIDATION001", "입력값이 유효하지 않습니다."),

    // 파일 관련
    FILE_READ_FAILED(HttpStatus.BAD_REQUEST, "FILE001", "파일을 읽는 데 실패했습니다."),
    INVALID_FILE_URL_FORMAT(HttpStatus.BAD_REQUEST, "FILE002", "잘못된 파일 URL 형식입니다."),

    // 메일 관련
    MAIL_SEND_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "MAIL001", "메일 발송에 실패했습니다."),
    MAIL_SENDER_ENCODING_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "MAIL002", "메일 발신자 정보 인코딩 중 오류가 발생했습니다"),

    // 시스템 관련
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "SYS001", "일시적인 문제가 발생했습니다. 잠시 후 다시 시도해주세요.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
