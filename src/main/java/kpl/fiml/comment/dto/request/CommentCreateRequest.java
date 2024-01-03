package kpl.fiml.comment.dto.request;

import kpl.fiml.comment.domain.Comment;
import kpl.fiml.notice.domain.Notice;
import kpl.fiml.user.domain.User;
import lombok.Getter;

@Getter
public class CommentCreateRequest {
    private String content;

    public Comment toEntity(User user, Notice notice) {
        return Comment.builder()
                .content(content)
                .user(user)
                .notice(notice)
                .build();
    }
}
