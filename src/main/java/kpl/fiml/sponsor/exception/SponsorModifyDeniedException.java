package kpl.fiml.sponsor.exception;

import lombok.Getter;

@Getter
public class SponsorModifyDeniedException extends RuntimeException {

    private final String errorCode;

    public SponsorModifyDeniedException(SponsorErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode.name();
    }
}
