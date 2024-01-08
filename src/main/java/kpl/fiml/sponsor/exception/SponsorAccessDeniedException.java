package kpl.fiml.sponsor.exception;

import lombok.Getter;

@Getter
public class SponsorAccessDeniedException extends RuntimeException {

    private final String errorCode;

    public SponsorAccessDeniedException(SponsorErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode.name();
    }
}
