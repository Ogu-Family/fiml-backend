package kpl.fiml.comment.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentDeleteResponse {
    private final Long id;

    public static CommentDeleteResponse of(Long id) {
        return new CommentDeleteResponse(id);
    }
}
