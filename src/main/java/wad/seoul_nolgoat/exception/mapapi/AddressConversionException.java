package wad.seoul_nolgoat.exception.mapapi;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import wad.seoul_nolgoat.exception.ErrorCode;

@Getter
public class AddressConversionException extends RuntimeException {

    private final HttpStatus httpStatus;

    public AddressConversionException() {
        super(ErrorCode.ADDRESS_CONVERSION_FAILED.getMessage());
        this.httpStatus = ErrorCode.ADDRESS_CONVERSION_FAILED.getHttpStatus();
    }

    public AddressConversionException(String customMessage) {
        super(customMessage);
        this.httpStatus = ErrorCode.ADDRESS_CONVERSION_FAILED.getHttpStatus();
    }
}
