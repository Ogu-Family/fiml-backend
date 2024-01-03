package kpl.fiml.comment.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentUpdateResponse {
    private final Long id;
    private final Long userId;
    private final String content;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static CommentUpdateResponse of(Long id, Long userId, String content, LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new CommentUpdateResponse(id, userId, content, createdAt, updatedAt);
    }
}
