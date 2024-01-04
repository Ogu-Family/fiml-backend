package kpl.fiml.settlement.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class SettlementDto {

    private final Long settleAmount;
    private final LocalDateTime settledAt;

    public static SettlementDto of(Long settleAmount, LocalDateTime settledAt) {
        return new SettlementDto(settleAmount, settledAt);
    }
}
