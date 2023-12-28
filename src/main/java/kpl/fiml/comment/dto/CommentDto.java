package kpl.fiml.comment.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentDto {
    private final Long id;
    private final String content;
    private final String commenter;
    private final Long noticeId;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static CommentDto of(Long id, String content, String commenter, Long noticeId, LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new CommentDto(id, content, commenter, noticeId, createdAt, updatedAt);
    }
}
