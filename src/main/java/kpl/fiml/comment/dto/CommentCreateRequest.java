package kpl.fiml.comment.dto;

import kpl.fiml.comment.domain.Comment;
import kpl.fiml.notice.domain.Notice;
import lombok.Getter;

@Getter
public class CommentCreateRequest {
    private String content;

    public Comment toEntity(Notice notice) {
        return Comment.builder()
                .content(content)
                .notice(notice)
                .build();
    }
}
