package kpl.fiml.user.exception;

import lombok.Getter;

@Getter
public class UserPermissionException extends RuntimeException {

    private final String errorCode;

    public UserPermissionException(UserErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode.name();
    }
}
