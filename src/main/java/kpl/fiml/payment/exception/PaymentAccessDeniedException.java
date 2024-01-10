package kpl.fiml.payment.exception;

import lombok.Getter;

@Getter
public class PaymentAccessDeniedException extends RuntimeException {

    private final String errorCode;

    public PaymentAccessDeniedException(PaymentErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode.name();
    }
}
