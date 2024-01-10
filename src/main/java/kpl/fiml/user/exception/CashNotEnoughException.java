package kpl.fiml.user.exception;

import lombok.Getter;

@Getter
public class CashNotEnoughException extends RuntimeException {

    private final String errorCode;

    public CashNotEnoughException(UserErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode.name();
    }
}
