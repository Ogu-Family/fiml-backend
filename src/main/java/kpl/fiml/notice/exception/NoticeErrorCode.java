package kpl.fiml.notice.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NoticeErrorCode {

    NOTICE_NOT_FOUND("해당하는 공지사항이 존재하지 않습니다."),
    ACCESS_DENIED("Notice 접근 권한이 없습니다.");

    private final String message;
}
