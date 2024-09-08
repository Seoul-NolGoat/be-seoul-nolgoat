package wad.seoul_nolgoat.exception.mapapi;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import wad.seoul_nolgoat.exception.ErrorCode;

@Getter
public class TMapApiException extends RuntimeException {

    private final HttpStatus httpStatus;

    public TMapApiException() {
        super(ErrorCode.TMAP_API_CALL_FAILED.getMessage());
        this.httpStatus = ErrorCode.TMAP_API_CALL_FAILED.getHttpStatus();
    }

    public TMapApiException(String customMessage) {
        super(customMessage);
        this.httpStatus = ErrorCode.TMAP_API_CALL_FAILED.getHttpStatus();
    }
}
