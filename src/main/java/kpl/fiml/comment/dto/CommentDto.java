package kpl.fiml.comment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Schema(description = "댓글 상세정보 DTO")
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentDto {
    @Schema(description = "댓글 id", example = "1")
    private final Long id;
    @Schema(description = "댓글 내용", example = "comment content")
    private final String content;
    @Schema(description = "댓글 작성자 id", example = "1")
    private final Long userId;
    @Schema(description = "댓글이 작성된 공지사항 id", example = "1")
    private final Long noticeId;
    @Schema(description = "댓글 생성 일시")
    private final LocalDateTime createdAt;
    @Schema(description = "댓글 수정 일시")
    private final LocalDateTime updatedAt;

    public static CommentDto of(Long id, String content, Long userId, Long noticeId, LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new CommentDto(id, content, userId, noticeId, createdAt, updatedAt);
    }
}
