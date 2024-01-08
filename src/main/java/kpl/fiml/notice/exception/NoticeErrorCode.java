package kpl.fiml.notice.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NoticeErrorCode {

    NOTICE_NOT_FOUND("NOTICE_NOT_FOUND", "해당하는 공지사항이 존재하지 않습니다.");

    private final String code;
    private final String message;
}
