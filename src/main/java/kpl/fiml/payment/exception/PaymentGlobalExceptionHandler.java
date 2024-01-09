package kpl.fiml.payment.exception;

import kpl.fiml.global.exception.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class PaymentGlobalExceptionHandler {

    @ExceptionHandler(PaymentAccessDeniedException.class)
    public ResponseEntity<ErrorResponse> catchPaymentAccessDeniedException(PaymentAccessDeniedException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErrorResponse(e.getErrorCode(), e.getMessage()));
    }
}
