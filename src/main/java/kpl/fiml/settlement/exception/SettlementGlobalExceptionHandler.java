package kpl.fiml.settlement.exception;

import kpl.fiml.global.exception.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class SettlementGlobalExceptionHandler {

    @ExceptionHandler(SettlementNotFoundException.class)
    public ResponseEntity<ErrorResponse> catchSettlementNotFoundException(SettlementNotFoundException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(e.getErrorCode(), e.getMessage()));
    }

    @ExceptionHandler(SettlementAccessDeniedException.class)
    public ResponseEntity<ErrorResponse> catchSettlementAccessDeniedException(SettlementAccessDeniedException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErrorResponse(e.getErrorCode(), e.getMessage()));
    }
}
