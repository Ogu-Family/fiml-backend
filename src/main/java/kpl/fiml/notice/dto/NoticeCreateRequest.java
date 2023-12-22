package kpl.fiml.notice.dto;

import kpl.fiml.notice.domain.Notice;
import lombok.Getter;

@Getter
public class NoticeCreateRequest{
    private String content;

    public Notice toEntity() {
        return Notice.builder()
                .content(content)
                .build();
    }
}
