package kpl.fiml.project.exception.reward;

import lombok.Getter;

@Getter
public class RewardFieldValueException extends RuntimeException {

    private final String errorCode;

    public RewardFieldValueException(RewardErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode.name();
    }
}
