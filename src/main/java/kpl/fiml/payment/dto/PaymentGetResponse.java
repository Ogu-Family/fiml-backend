package kpl.fiml.payment.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class PaymentGetResponse {

    private final String paymentStatus;
    private final LocalDateTime requestedAt;
    private final LocalDateTime approvedAt;

    public static PaymentGetResponse of(String paymentStatus, LocalDateTime requestedAt, LocalDateTime approvedAt) {
        return new PaymentGetResponse(paymentStatus, requestedAt, approvedAt);
    }
}
