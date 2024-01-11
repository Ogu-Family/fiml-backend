package kpl.fiml.comment.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import kpl.fiml.comment.domain.Comment;
import kpl.fiml.notice.domain.Notice;
import kpl.fiml.user.domain.User;
import lombok.*;

@Schema(description = "댓글 생성 요청 DTO")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentCreateRequest {
    @Schema(description = "댓글 내용", example = "comment content")
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
