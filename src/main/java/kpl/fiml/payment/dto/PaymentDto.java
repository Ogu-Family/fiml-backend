package kpl.fiml.payment.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class PaymentDto {

    private final String paymentStatus;
    private final LocalDateTime requestedAt;
    private final LocalDateTime approvedAt;

    public static PaymentDto of(String paymentStatus, LocalDateTime requestedAt, LocalDateTime approvedAt) {
        return new PaymentDto(paymentStatus, requestedAt, approvedAt);
    }
}
