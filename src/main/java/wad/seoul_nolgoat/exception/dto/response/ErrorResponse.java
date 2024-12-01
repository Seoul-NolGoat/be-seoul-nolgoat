package wad.seoul_nolgoat.exception.dto.response;

import lombok.Getter;
import wad.seoul_nolgoat.exception.ErrorCode;

import java.util.Map;

@Getter
public class ErrorResponse {

    private String code;
    private String message;
    private Map<String, String> errors;

    public ErrorResponse(ErrorCode errorCode, Map<String, String> errors) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
        this.errors = errors;
    }

    public ErrorResponse(ErrorCode errorCode) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }
}
