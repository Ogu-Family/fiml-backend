package kpl.fiml.user.exception;

import lombok.Getter;

@Getter
public class InvalidPasswordException extends RuntimeException {
    private final String errorCode;
    public InvalidPasswordException() {
        super(UserErrorCode.INVALID_PASSWORD.getMessage());
        this.errorCode = UserErrorCode.INVALID_PASSWORD.getCode();
    }
}
