package kpl.fiml.comment.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Schema(description = "댓글 삭제 응답 DTO")
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentDeleteResponse {
    @Schema(description = "삭제된 댓글 id", example = "1")
    private final Long id;

    public static CommentDeleteResponse of(Long id) {
        return new CommentDeleteResponse(id);
    }
}
