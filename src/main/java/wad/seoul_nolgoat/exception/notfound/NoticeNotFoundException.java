package wad.seoul_nolgoat.exception.notfound;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import wad.seoul_nolgoat.exception.ErrorCode;

@Getter
public class NoticeNotFoundException extends RuntimeException {

    private final HttpStatus httpStatus;

    public NoticeNotFoundException() {
        super(ErrorCode.NOTICE_NOT_FOUND.getMessage());
        this.httpStatus = ErrorCode.NOTICE_NOT_FOUND.getHttpStatus();
    }

    public NoticeNotFoundException(String customMessage) {
        super(customMessage);
        this.httpStatus = ErrorCode.NOTICE_NOT_FOUND.getHttpStatus();
    }
}
