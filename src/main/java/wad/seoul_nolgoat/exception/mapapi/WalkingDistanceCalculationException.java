package wad.seoul_nolgoat.exception.mapapi;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import wad.seoul_nolgoat.exception.ErrorCode;

@Getter
public class WalkingDistanceCalculationException extends RuntimeException {

    private final HttpStatus httpStatus;

    public WalkingDistanceCalculationException() {
        super(ErrorCode.WALKING_DISTANCE_CALCULATION_FAILED.getMessage());
        this.httpStatus = ErrorCode.WALKING_DISTANCE_CALCULATION_FAILED.getHttpStatus();
    }

    public WalkingDistanceCalculationException(String customMessage) {
        super(customMessage);
        this.httpStatus = ErrorCode.WALKING_DISTANCE_CALCULATION_FAILED.getHttpStatus();
    }
}
