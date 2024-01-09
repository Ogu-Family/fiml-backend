package kpl.fiml.project.exception.project;

import lombok.Getter;

@Getter
public class ProjectDeleteException extends RuntimeException {

    private final String errorCode;

    public ProjectDeleteException(ProjectErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode.name();
    }
}
