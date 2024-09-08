package wad.seoul_nolgoat.exception.auth;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import wad.seoul_nolgoat.exception.ErrorCode;

@Getter
public class UnsupportedProviderException extends RuntimeException {

    private final HttpStatus httpStatus;

    public UnsupportedProviderException() {
        super(ErrorCode.UNSUPPORTED_PROVIDER.getMessage());
        this.httpStatus = ErrorCode.UNSUPPORTED_PROVIDER.getHttpStatus();
    }

    public UnsupportedProviderException(String customMessage) {
        super(customMessage);
        this.httpStatus = ErrorCode.UNSUPPORTED_PROVIDER.getHttpStatus();
    }
}
