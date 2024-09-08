package wad.seoul_nolgoat.exception.notfound;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import wad.seoul_nolgoat.exception.ErrorCode;

@Getter
public class StoreNotFoundException extends RuntimeException {

    private final HttpStatus httpStatus;

    public StoreNotFoundException() {
        super(ErrorCode.STORE_NOT_FOUND.getMessage());
        this.httpStatus = ErrorCode.STORE_NOT_FOUND.getHttpStatus();
    }

    public StoreNotFoundException(String customMessage) {
        super(customMessage);
        this.httpStatus = ErrorCode.STORE_NOT_FOUND.getHttpStatus();
    }
}
