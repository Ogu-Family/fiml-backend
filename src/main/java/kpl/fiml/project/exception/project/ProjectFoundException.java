package kpl.fiml.project.exception.project;

import lombok.Getter;

@Getter
public class ProjectFoundException extends RuntimeException {

    private final String errorCode;

    public ProjectFoundException(ProjectErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode.name();
    }
}
