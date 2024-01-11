package kpl.fiml.comment.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Schema(description = "댓글 생성 응답 DTO")
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentCreateResponse {
    @Schema(description = "생성된 댓글 id", example = "1")
    private final Long id;
    @Schema(description = "생성된 댓글 내용", example = "comment content")
    private final String content;
    @Schema(description = "생성된 댓글 작성자 id", example = "1")
    private final Long userId;
    @Schema(description = "생성된 댓글의 공지사항 id", example = "1")
    private final Long noticeId;

    public static CommentCreateResponse of(Long id, String content, Long userId, Long noticeId) {
        return new CommentCreateResponse(id, content, userId, noticeId);
    }
}
