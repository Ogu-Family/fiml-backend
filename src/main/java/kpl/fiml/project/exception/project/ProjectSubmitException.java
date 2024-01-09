package kpl.fiml.project.exception.project;

import lombok.Getter;

@Getter
public class ProjectSubmitException extends RuntimeException {

    private final String errorCode;

    public ProjectSubmitException(ProjectErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode.name();
    }
}
