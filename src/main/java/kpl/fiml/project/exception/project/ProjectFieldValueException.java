package kpl.fiml.project.exception.project;

import lombok.Getter;

@Getter
public class ProjectFieldValueException extends RuntimeException {

    private final String errorCode;

    public ProjectFieldValueException(ProjectErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode.name();
    }
}
