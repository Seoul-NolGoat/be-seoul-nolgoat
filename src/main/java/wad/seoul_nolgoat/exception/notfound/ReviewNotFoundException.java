package wad.seoul_nolgoat.exception.notfound;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import wad.seoul_nolgoat.exception.ErrorCode;

@Getter
public class ReviewNotFoundException extends RuntimeException {

    private final HttpStatus httpStatus;

    public ReviewNotFoundException() {
        super(ErrorCode.REVIEW_NOT_FOUND.getMessage());
        this.httpStatus = ErrorCode.REVIEW_NOT_FOUND.getHttpStatus();
    }

    public ReviewNotFoundException(String customMessage) {
        super(customMessage);
        this.httpStatus = ErrorCode.REVIEW_NOT_FOUND.getHttpStatus();
    }
}
