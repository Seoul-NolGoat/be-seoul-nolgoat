package wad.seoul_nolgoat.exception.search;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import wad.seoul_nolgoat.exception.ErrorCode;

@Getter
public class InvalidSearchCriteriaException extends RuntimeException {

    private final HttpStatus httpStatus;

    public InvalidSearchCriteriaException() {
        super(ErrorCode.INVALID_SEARCH_CRITERIA.getMessage());
        this.httpStatus = ErrorCode.INVALID_SEARCH_CRITERIA.getHttpStatus();
    }

    public InvalidSearchCriteriaException(String customMessage) {
        super(customMessage);
        this.httpStatus = ErrorCode.INVALID_SEARCH_CRITERIA.getHttpStatus();
    }
}
