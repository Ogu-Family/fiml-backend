package kpl.fiml.notice.exception;

import lombok.Getter;

@Getter
public class NoticeNotFoundException extends RuntimeException {

    private final String errorCode;

    public NoticeNotFoundException(NoticeErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode.name();
    }

}

