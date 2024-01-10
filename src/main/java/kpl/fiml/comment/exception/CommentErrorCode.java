package kpl.fiml.comment.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CommentErrorCode {
    COMMENT_NOT_FOUND("해당 댓글이 존재하지 않습니다."),
    ACCESS_DENIED("Comment 접근 권한이 없습니다.");

    private final String message;
}
