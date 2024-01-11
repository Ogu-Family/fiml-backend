package kpl.fiml.payment.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Schema(description = "결제 정보 DTO")
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class PaymentDto {

    @Schema(description = "결제 성공/실패 여부", example = "결제 성공")
    private final String paymentStatus;

    @Schema(description = "결제 요청 일시")
    private final LocalDateTime requestedAt;

    @Schema(description = "결제 승인 일시")
    private final LocalDateTime approvedAt;

    public static PaymentDto of(String paymentStatus, LocalDateTime requestedAt, LocalDateTime approvedAt) {
        return new PaymentDto(paymentStatus, requestedAt, approvedAt);
    }
}
