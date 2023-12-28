package kpl.fiml.comment.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentCreateResponse {
    private final Long id;
    private final String content;
    private final String commenter;
    private final Long noticeId;

    public static CommentCreateResponse of(Long id, String content, String commenter, Long noticeId) {
        return new CommentCreateResponse(id, content, commenter, noticeId);
    }
}
