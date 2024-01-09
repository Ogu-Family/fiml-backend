package kpl.fiml.project.exception.project;

import lombok.Getter;

@Getter
public class ProjectFindConditionException extends RuntimeException {

    private final String errorCode;

    public ProjectFindConditionException(ProjectErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode.name();
    }
}
