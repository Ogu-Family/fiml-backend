package kpl.fiml.settlement.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SettlementErrorCode {

    SETTLEMENT_NOT_FOUND("정산 정보가 존재하지 않습니다."),
    SETTLEMENT_ACCESS_DENIED("접근 가능한 정산 정보가 아닙니다.");

    private final String message;
}
