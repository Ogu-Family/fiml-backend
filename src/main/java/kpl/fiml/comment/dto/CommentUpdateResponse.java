package kpl.fiml.comment.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentUpdateResponse {
    private final Long id;
    private final String content;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static CommentUpdateResponse of(Long id, String content, LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new CommentUpdateResponse(id, content, createdAt, updatedAt);
    }
}
