package kpl.fiml.comment.exception;

import lombok.Getter;

@Getter
public class CommentPermissionException extends RuntimeException {

    private final String errorCode;

    public CommentPermissionException(CommentErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode.getCode();
    }
}
