package kpl.fiml.user.exception;

import lombok.Getter;

@Getter
public class InvalidEmailException extends RuntimeException {
    private final String errorCode;
    public InvalidEmailException() {
        super(UserErrorCode.INVALID_EMAIL.getMessage());
        this.errorCode = UserErrorCode.INVALID_EMAIL.getCode();
    }
}
