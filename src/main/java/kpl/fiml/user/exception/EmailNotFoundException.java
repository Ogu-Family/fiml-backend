package kpl.fiml.user.exception;

import lombok.Getter;

@Getter
public class EmailNotFoundException extends RuntimeException {
    private final String errorCode;

    public EmailNotFoundException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
