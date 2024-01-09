package kpl.fiml.project.exception.reward;

import lombok.Getter;

@Getter
public class RewardNotFoundException extends RuntimeException {

    private final String errorCode;

    public RewardNotFoundException(RewardErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode.name();
    }
}
