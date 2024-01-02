package kpl.fiml.comment.dto;

import lombok.Getter;

@Getter
public class CommentUpdateRequest {
    private String content;
    private Long userId;  // TODO: authentication 적용 시 삭제
}
