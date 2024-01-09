package kpl.fiml.comment.exception;

import lombok.Getter;

@Getter
public class CommentNotFoundException extends RuntimeException {

    private final String errorCode;

    public CommentNotFoundException(CommentErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode.getCode();
    }
}
