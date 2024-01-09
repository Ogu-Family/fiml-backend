package kpl.fiml.project.exception.project;

import lombok.Getter;

@Getter
public class ProjectAccessException extends RuntimeException {

    private final String errorCode;

    public ProjectAccessException(ProjectErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode.name();
    }
}
