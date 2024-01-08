package kpl.fiml.user.exception;

import lombok.Getter;

@Getter
public class EmailNotFoundException extends RuntimeException {
    private final String errorCode;

    public EmailNotFoundException() {
        super(UserErrorCode.EMAIL_NOT_FOUND.getMessage());
        this.errorCode = UserErrorCode.EMAIL_NOT_FOUND.getCode();
    }
}
