package kpl.fiml.user.exception;

import lombok.Getter;

@Getter
public class DuplicateEmailException extends RuntimeException {

    private final String errorCode;

    public DuplicateEmailException(UserErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode.name();
    }
}
