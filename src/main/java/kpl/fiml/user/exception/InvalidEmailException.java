package kpl.fiml.user.exception;

import lombok.Getter;

@Getter
public class InvalidEmailException extends RuntimeException {
    private final String errorCode;
    public InvalidEmailException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
