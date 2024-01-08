package kpl.fiml.user.exception;

import lombok.Getter;

@Getter
public class InvalidPasswordException extends RuntimeException {
    private final String errorCode;
    public InvalidPasswordException(UserErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode.getCode();
    }
}
