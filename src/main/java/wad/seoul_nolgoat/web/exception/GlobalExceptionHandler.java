package wad.seoul_nolgoat.web.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import wad.seoul_nolgoat.exception.auth.TokenExpiredException;
import wad.seoul_nolgoat.exception.auth.UnsupportedProviderException;
import wad.seoul_nolgoat.exception.mapapi.AddressConversionException;
import wad.seoul_nolgoat.exception.mapapi.TMapApiException;
import wad.seoul_nolgoat.exception.mapapi.WalkingDistanceCalculationException;
import wad.seoul_nolgoat.exception.notfound.ReviewNotFoundException;
import wad.seoul_nolgoat.exception.notfound.StoreNotFoundException;
import wad.seoul_nolgoat.exception.notfound.UserNotFoundException;
import wad.seoul_nolgoat.exception.search.InvalidRoundException;
import wad.seoul_nolgoat.exception.search.InvalidSearchCriteriaException;
import wad.seoul_nolgoat.web.exception.dto.response.ErrorResult;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorResult> handleUserNotFoundException(UserNotFoundException e) {
        return ResponseEntity
                .status(e.getHttpStatus())
                .body(new ErrorResult(e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResult> handleStoreNotFoundException(StoreNotFoundException e) {
        return ResponseEntity
                .status(e.getHttpStatus())
                .body(new ErrorResult(e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResult> handleReviewNotFoundException(ReviewNotFoundException e) {
        return ResponseEntity
                .status(e.getHttpStatus())
                .body(new ErrorResult(e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResult> handleInvalidSearchCriteriaException(InvalidSearchCriteriaException e) {
        return ResponseEntity
                .status(e.getHttpStatus())
                .body(new ErrorResult(e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResult> handleInvalidRoundException(InvalidRoundException e) {
        return ResponseEntity
                .status(e.getHttpStatus())
                .body(new ErrorResult(e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResult> handleTokenExpiredException(TokenExpiredException e) {
        return ResponseEntity
                .status(e.getHttpStatus())
                .body(new ErrorResult(e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResult> handleUnsupportedProviderException(UnsupportedProviderException e) {
        return ResponseEntity
                .status(e.getHttpStatus())
                .body(new ErrorResult(e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResult> handleAddressConversionException(AddressConversionException e) {
        return ResponseEntity
                .status(e.getHttpStatus())
                .body(new ErrorResult(e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResult> handleWalkingDistanceCalculationException(WalkingDistanceCalculationException e) {
        return ResponseEntity
                .status(e.getHttpStatus())
                .body(new ErrorResult(e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResult> handleTMapApiException(TMapApiException e) {
        return ResponseEntity
                .status(e.getHttpStatus())
                .body(new ErrorResult(e.getMessage()));
    }
}
