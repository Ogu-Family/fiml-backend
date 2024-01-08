package kpl.fiml.sponsor.exception;

import kpl.fiml.global.exception.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class SponsorGlobalExceptionHandler {

    @ExceptionHandler(SponsorNotFoundException.class)
    public ResponseEntity<ErrorResponse> catchSponsorNotFoundException(SponsorNotFoundException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(e.getErrorCode(), e.getMessage()));
    }

    @ExceptionHandler(InvalidTotalAmountException.class)
    public ResponseEntity<ErrorResponse> catchInvalidTotalAmountException(InvalidTotalAmountException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(e.getErrorCode(), e.getMessage()));
    }

    @ExceptionHandler(SponsorAccessDeniedException.class)
    public ResponseEntity<ErrorResponse> catchSponsorAccessDeniedException(SponsorAccessDeniedException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErrorResponse(e.getErrorCode(), e.getMessage()));
    }

    @ExceptionHandler(SponsorModifyDeniedException.class)
    public ResponseEntity<ErrorResponse> catchSponsorModifyDeniedException(SponsorModifyDeniedException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(e.getErrorCode(), e.getMessage()));
    }
}
