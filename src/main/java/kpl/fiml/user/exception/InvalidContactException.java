package kpl.fiml.user.exception;

import lombok.Getter;

@Getter
public class InvalidContactException extends RuntimeException {

    private final String errorCode;

    public InvalidContactException(UserErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode.name();
    }
}
