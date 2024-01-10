package kpl.fiml.user.exception;

import lombok.Getter;

@Getter
public class InvalidAmountException extends RuntimeException {

    private final String errorCode;

    public InvalidAmountException(UserErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode.name();
    }
}
