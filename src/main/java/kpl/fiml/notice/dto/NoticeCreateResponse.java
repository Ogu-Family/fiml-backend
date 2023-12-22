package kpl.fiml.notice.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class NoticeCreateResponse {
    private Long id;

    @Builder
    public NoticeCreateResponse(Long id) {
        this.id = id;
    }
}
