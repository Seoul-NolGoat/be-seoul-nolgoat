package wad.seoul_nolgoat.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import wad.seoul_nolgoat.exception.*;
import wad.seoul_nolgoat.web.exception.dto.response.ErrorResult;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorResult> handleUserNotFoundException(UserNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResult(e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResult> handleStoreNotFoundException(StoreNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResult(e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResult> handleReviewNotFoundException(ReviewNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResult(e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResult> handleInvalidSearchCriteriaException(InvalidSearchCriteriaException e) {
        return ResponseEntity
                .badRequest()
                .body(new ErrorResult(e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResult> handleInvalidRoundException(InvalidRoundException e) {
        return ResponseEntity
                .badRequest()
                .body(new ErrorResult(e.getMessage()));
    }
}
