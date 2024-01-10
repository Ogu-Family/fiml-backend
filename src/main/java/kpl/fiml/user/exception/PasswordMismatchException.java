package kpl.fiml.user.exception;

import lombok.Getter;

@Getter
public class PasswordMismatchException extends RuntimeException {
    private final String errorCode;

    public PasswordMismatchException(UserErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode.name();
    }
}
