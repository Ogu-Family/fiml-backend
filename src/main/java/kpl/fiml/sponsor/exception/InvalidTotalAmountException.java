package kpl.fiml.sponsor.exception;

import lombok.Getter;

@Getter
public class InvalidTotalAmountException extends RuntimeException {

    private final String errorCode;

    public InvalidTotalAmountException(SponsorErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode.name();
    }
}
