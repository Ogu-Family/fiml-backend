package kpl.fiml.project.exception.project;

import lombok.Getter;

@Getter
public class ProjectLikeException extends RuntimeException {

    private final String errorCode;

    public ProjectLikeException(ProjectErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode.name();
    }
}
