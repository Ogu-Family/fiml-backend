package kpl.fiml.comment.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Schema(description = "댓글 수정 응답 DTO")
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentUpdateResponse {
    @Schema(description = "수정된 댓글 id", example = "1")
    private final Long id;
    @Schema(description = "댓글 작성자 id", example = "1")
    private final Long userId;
    @Schema(description = "수정된 댓글 내용", example = "updated comment content")
    private final String content;
    @Schema(description = "댓글 생성 일시")
    private final LocalDateTime createdAt;
    @Schema(description = "댓글 수정 일시")
    private final LocalDateTime updatedAt;

    public static CommentUpdateResponse of(Long id, Long userId, String content, LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new CommentUpdateResponse(id, userId, content, createdAt, updatedAt);
    }
}
