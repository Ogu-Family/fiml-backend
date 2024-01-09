package kpl.fiml.project.exception.project;

import lombok.Getter;

@Getter
public class ProjectFundingException extends RuntimeException {

    private final String errorCode;

    public ProjectFundingException(ProjectErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode.name();
    }
}
