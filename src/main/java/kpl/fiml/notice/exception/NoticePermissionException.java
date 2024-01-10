package kpl.fiml.notice.exception;

import lombok.Getter;

@Getter
public class NoticePermissionException extends RuntimeException {

    private final String errorCode;

    public NoticePermissionException(NoticeErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode.name();
    }

}
