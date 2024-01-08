package kpl.fiml.user.exception;

import lombok.Getter;

@Getter
public class DuplicateEmailException extends RuntimeException {

    private final String errorCode;

    public DuplicateEmailException() {
        super(UserErrorCode.DUPLICATED_EMAIL.getMessage());
        this.errorCode = UserErrorCode.DUPLICATED_EMAIL.getCode();
    }
}
