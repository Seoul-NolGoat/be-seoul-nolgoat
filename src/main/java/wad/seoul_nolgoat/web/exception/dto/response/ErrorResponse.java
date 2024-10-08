package wad.seoul_nolgoat.web.exception.dto.response;

import lombok.Getter;
import wad.seoul_nolgoat.exception.ErrorCode;

@Getter
public class ErrorResponse {

    private String code;
    private String message;

    public ErrorResponse(ErrorCode errorCode) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }
}
