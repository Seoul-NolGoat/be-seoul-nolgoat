package wad.seoul_nolgoat.exception;

import lombok.Getter;

@Getter
public class TMapException extends RuntimeException {

    private final ErrorCode errorCode;

    public TMapException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    // 기존 예외를 포함하기 위한 생성자
    public TMapException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode;
    }
}
