package kpl.fiml.settlement.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class SettlementDto {

    @Schema(description = "총 정산금", example = "1985724587")
    private final Long settleAmount;

    @Schema(description = "정산 처리 일시")
    private final LocalDateTime settledAt;

    public static SettlementDto of(Long settleAmount, LocalDateTime settledAt) {
        return new SettlementDto(settleAmount, settledAt);
    }
}
