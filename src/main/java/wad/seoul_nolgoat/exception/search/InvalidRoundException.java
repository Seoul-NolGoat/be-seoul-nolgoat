package wad.seoul_nolgoat.exception.search;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import wad.seoul_nolgoat.exception.ErrorCode;

@Getter
public class InvalidRoundException extends RuntimeException {

    private final HttpStatus httpStatus;

    public InvalidRoundException() {
        super(ErrorCode.INVALID_GATHERING_ROUND.getMessage());
        this.httpStatus = ErrorCode.INVALID_GATHERING_ROUND.getHttpStatus();
    }

    public InvalidRoundException(String customMessage) {
        super(customMessage);
        this.httpStatus = ErrorCode.INVALID_GATHERING_ROUND.getHttpStatus();
    }
}
