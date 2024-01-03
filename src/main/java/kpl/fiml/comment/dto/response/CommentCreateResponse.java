package kpl.fiml.comment.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentCreateResponse {
    private final Long id;
    private final String content;
    private final Long userId;
    private final Long noticeId;

    public static CommentCreateResponse of(Long id, String content, Long userId, Long noticeId) {
        return new CommentCreateResponse(id, content, userId, noticeId);
    }
}
