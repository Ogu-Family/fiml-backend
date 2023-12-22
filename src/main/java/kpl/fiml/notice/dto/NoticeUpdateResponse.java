package kpl.fiml.notice.dto;

import lombok.Getter;

@Getter
public class NoticeUpdateResponse {
    private Long noticeId;
    private String content;

    public NoticeUpdateResponse(Long noticeId, String content) {
        this.noticeId = noticeId;
        this.content = content;
    }
}
