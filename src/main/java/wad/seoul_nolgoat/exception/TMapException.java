package wad.seoul_nolgoat.exception;

import lombok.Getter;

@Getter
public class TMapException extends ApiException {

    private final ErrorCode errorCode;

    public TMapException(ErrorCode errorCode) {
        super(errorCode);
        this.errorCode = errorCode;
    }
}
