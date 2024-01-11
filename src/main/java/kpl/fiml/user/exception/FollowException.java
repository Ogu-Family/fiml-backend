package kpl.fiml.user.exception;

import lombok.Getter;

@Getter
public class FollowException extends RuntimeException {

    private final String errorCode;

    public FollowException(UserErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode.name();
    }
}
