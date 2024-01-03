package kpl.fiml.comment.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CommentUpdateRequest {
    @NotBlank(message = "댓글 내용은 필수 입력값 입니다.")
    private String content;
}
