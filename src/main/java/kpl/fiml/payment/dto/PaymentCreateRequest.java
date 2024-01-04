package kpl.fiml.payment.dto;

import jakarta.validation.constraints.NotBlank;
import kpl.fiml.payment.domain.Payment;
import kpl.fiml.sponsor.domain.Sponsor;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class PaymentCreateRequest {

    @NotBlank
    private final Sponsor sponsor;

    @NotBlank
    private final LocalDate requestedDay;

    public static PaymentCreateRequest of(Sponsor sponsor, LocalDate requestedDay) {
        return new PaymentCreateRequest(sponsor, requestedDay);
    }

    public Payment toEntity(LocalDateTime requestedAt) {
        return Payment.builder()
                .sponsor(sponsor)
                .requestedAt(requestedAt)
                .build();
    }
}
