package wad.seoul_nolgoat.exception.duplicate;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import wad.seoul_nolgoat.exception.ErrorCode;

@Getter
public class ReviewDuplicateException extends RuntimeException {

    private final HttpStatus httpStatus;

    public ReviewDuplicateException() {
        super(ErrorCode.DUPLICATE_REVIEW.getMessage());
        this.httpStatus = ErrorCode.DUPLICATE_REVIEW.getHttpStatus();
    }

    public ReviewDuplicateException(String customMessage) {
        super(customMessage);
        this.httpStatus = ErrorCode.DUPLICATE_REVIEW.getHttpStatus();
    }
}
