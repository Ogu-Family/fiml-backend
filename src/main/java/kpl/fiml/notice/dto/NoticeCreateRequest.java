package kpl.fiml.notice.dto;

import kpl.fiml.notice.domain.Notice;

public record NoticeCreateRequest(String content) {

    public Notice toEntity() {
        return Notice.builder()
                .content(content)
                .build();
    }
}
