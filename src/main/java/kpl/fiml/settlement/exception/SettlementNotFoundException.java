package kpl.fiml.settlement.exception;

import lombok.Getter;

@Getter
public class SettlementNotFoundException extends RuntimeException {

    private final String errorCode;

    public SettlementNotFoundException(SettlementErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode.name();
    }
}
