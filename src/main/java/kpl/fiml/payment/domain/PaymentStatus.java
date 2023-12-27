package kpl.fiml.payment.domain;

import lombok.Getter;

@Getter
public enum PaymentStatus {

    WAIT("결제 대기"),
    SUCCESS("결제 성공"),
    FAIL("결제 실패");

    private final String displayName;

    PaymentStatus(String displayName) {
        this.displayName = displayName;
    }
}
