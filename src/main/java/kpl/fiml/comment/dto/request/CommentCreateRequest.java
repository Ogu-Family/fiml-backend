package kpl.fiml.comment.dto.request;

import jakarta.validation.constraints.NotBlank;
import kpl.fiml.comment.domain.Comment;
import kpl.fiml.notice.domain.Notice;
import kpl.fiml.user.domain.User;
import lombok.Getter;

@Getter
public class CommentCreateRequest {
    @NotBlank(message = "댓글 내용은 필수 입력값 입니다.")
    private String content;

    public Comment toEntity(User user, Notice notice) {
        return Comment.builder()
                .content(content)
                .user(user)
                .notice(notice)
                .build();
    }
}
