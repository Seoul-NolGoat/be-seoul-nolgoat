package wad.seoul_nolgoat.exception.notfound;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import wad.seoul_nolgoat.exception.ErrorCode;

@Getter
public class UserNotFoundException extends RuntimeException {

    private final HttpStatus httpStatus;

    public UserNotFoundException() {
        super(ErrorCode.USER_NOT_FOUND.getMessage());
        this.httpStatus = ErrorCode.USER_NOT_FOUND.getHttpStatus();
    }

    public UserNotFoundException(String customMessage) {
        super(customMessage);
        this.httpStatus = ErrorCode.USER_NOT_FOUND.getHttpStatus();
    }
}
