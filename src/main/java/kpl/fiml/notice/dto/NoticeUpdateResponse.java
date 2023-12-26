package kpl.fiml.notice.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class NoticeUpdateResponse {
    private final Long noticeId;
    private final String content;

    public static NoticeUpdateResponse of(Long noticeId, String content) {
        return new NoticeUpdateResponse(noticeId, content);
    }
}
