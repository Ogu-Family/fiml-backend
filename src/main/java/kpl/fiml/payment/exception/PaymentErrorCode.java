package kpl.fiml.payment.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentErrorCode {

    PAYMENT_ACCESS_DENIED("접근 가능한 결제 정보가 아닙니다.");

    private final String message;
}
