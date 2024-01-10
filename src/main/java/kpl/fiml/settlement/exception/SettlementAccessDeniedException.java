package kpl.fiml.settlement.exception;

import lombok.Getter;

@Getter
public class SettlementAccessDeniedException extends RuntimeException {

    private final String errorCode;

    public SettlementAccessDeniedException(SettlementErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode.name();
    }
}
