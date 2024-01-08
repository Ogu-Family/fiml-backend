package kpl.fiml.notice.exception;

import lombok.Getter;

@Getter
public class NoticeNotFoundException extends RuntimeException {

    private final String errorCode;

    public NoticeNotFoundException() {
        super(NoticeErrorCode.NOTICE_NOT_FOUND.getMessage());
        this.errorCode = NoticeErrorCode.NOTICE_NOT_FOUND.getCode();
    }

}

