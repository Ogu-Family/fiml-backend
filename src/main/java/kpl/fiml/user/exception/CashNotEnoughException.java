package kpl.fiml.user.exception;

import lombok.Getter;

@Getter
public class CashNotEnoughException extends RuntimeException {

    private final String errorCode;

    public CashNotEnoughException() {
        super(UserErrorCode.CASH_NOT_ENOUGH.getMessage());
        this.errorCode = UserErrorCode.CASH_NOT_ENOUGH.getCode();
    }
}
