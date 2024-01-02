package kpl.fiml.comment.dto;

import kpl.fiml.comment.domain.Comment;
import kpl.fiml.notice.domain.Notice;
import kpl.fiml.user.domain.User;
import lombok.Getter;

@Getter
public class CommentCreateRequest {
    private String content;
    private Long userId; // TODO: authentication 적용시 삭제

    public Comment toEntity(User user, Notice notice) {
        return Comment.builder()
                .content(content)
                .user(user)
                .notice(notice)
                .build();
    }
}
