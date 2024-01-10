package kpl.fiml.user.exception;

import lombok.Getter;

@Getter
public class EmailNotFoundException extends RuntimeException {
    private final String errorCode;

    public EmailNotFoundException(UserErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode.name();
    }
}
