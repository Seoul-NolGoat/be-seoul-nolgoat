package wad.seoul_nolgoat.exception.notfound;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import wad.seoul_nolgoat.exception.ErrorCode;

@Getter
public class InquiryNotFoundException extends RuntimeException {

    private final HttpStatus httpStatus;

    public InquiryNotFoundException() {
        super(ErrorCode.INQUIRY_NOT_FOUND.getMessage());
        this.httpStatus = ErrorCode.INQUIRY_NOT_FOUND.getHttpStatus();
    }

    public InquiryNotFoundException(String customMessage) {
        super(customMessage);
        this.httpStatus = ErrorCode.INQUIRY_NOT_FOUND.getHttpStatus();
    }
}
