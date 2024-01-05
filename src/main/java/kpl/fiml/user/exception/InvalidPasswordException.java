package kpl.fiml.user.exception;

import lombok.Getter;

@Getter
public class InvalidPasswordException extends RuntimeException {
    private final String errorCode;
    public InvalidPasswordException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
