package kpl.fiml.user.exception;

import lombok.Getter;

@Getter
public class InvalidAmountException extends RuntimeException {

    private final String errorCode;

    public InvalidAmountException() {
        super(UserErrorCode.INVALID_AMOUNT.getMessage());
        this.errorCode = UserErrorCode.INVALID_AMOUNT.getCode();
    }
}
