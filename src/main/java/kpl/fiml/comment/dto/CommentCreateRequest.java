package kpl.fiml.comment.dto;

import kpl.fiml.comment.domain.Comment;
import kpl.fiml.notice.domain.Notice;
import lombok.Getter;

@Getter
public class CommentCreateRequest {
    private String content;
    private String commenter;

    public Comment toEntity(Notice notice) {
        return Comment.builder()
                .content(content)
                .commenter(commenter)
                .notice(notice)
                .build();
    }
}
