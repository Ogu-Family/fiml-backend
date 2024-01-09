package kpl.fiml.sponsor.exception;

import lombok.Getter;

@Getter
public class SponsorNotFoundException extends RuntimeException {

    private final String errorCode;

    public SponsorNotFoundException(SponsorErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode.name();
    }
}
