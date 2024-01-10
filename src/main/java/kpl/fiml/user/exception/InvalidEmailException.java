package kpl.fiml.user.exception;

import lombok.Getter;

@Getter
public class InvalidEmailException extends RuntimeException {
    private final String errorCode;
    public InvalidEmailException(UserErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode.name();
    }
}
