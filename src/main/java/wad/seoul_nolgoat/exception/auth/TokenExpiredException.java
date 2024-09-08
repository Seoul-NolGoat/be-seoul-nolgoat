package wad.seoul_nolgoat.exception.auth;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import wad.seoul_nolgoat.exception.ErrorCode;

@Getter
public class TokenExpiredException extends RuntimeException {

    private final HttpStatus httpStatus;

    public TokenExpiredException() {
        super(ErrorCode.TOKEN_EXPIRED_MESSAGE.getMessage());
        this.httpStatus = ErrorCode.TOKEN_EXPIRED_MESSAGE.getHttpStatus();
    }

    public TokenExpiredException(String customMessage) {
        super(customMessage);
        this.httpStatus = ErrorCode.TOKEN_EXPIRED_MESSAGE.getHttpStatus();
    }
}
