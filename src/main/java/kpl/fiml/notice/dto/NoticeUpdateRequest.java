package kpl.fiml.notice.dto;

import lombok.Getter;

@Getter
public class NoticeUpdateRequest {
    private String content;
    private Long userId; // TODO : authentication 적용시 삭제
}
