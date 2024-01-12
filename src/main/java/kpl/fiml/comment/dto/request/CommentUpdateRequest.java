package kpl.fiml.comment.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Schema(description = "댓글 수정 요청 DTO")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentUpdateRequest {
    @Schema(description = "수정할 댓글 내용", example = "updated comment content")
    @NotBlank(message = "댓글 내용은 필수 입력값 입니다.")
    private String content;
}
