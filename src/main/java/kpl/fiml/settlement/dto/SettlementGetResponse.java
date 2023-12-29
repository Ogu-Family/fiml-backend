package kpl.fiml.settlement.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class SettlementGetResponse {

    private final Long settleAmount;
    private final LocalDateTime settledAt;

    public static SettlementGetResponse of(Long settleAmount, LocalDateTime settledAt) {
        return new SettlementGetResponse(settleAmount, settledAt);
    }
}
