package kpl.fiml.sponsor.domain;

import lombok.Getter;

@Getter
public enum SponsorStatus {

    FUNDING_PROCEEDING("펀딩 진행 중"),
    PAYMENT_PROCEEDING("결제 진행 중"),
    FUNDING_FAIL("펀딩 실패"),
    PAYMENT_FAIL("결제 실패"),
    COMPLETE("완료");

    private final String displayName;

    SponsorStatus(String displayName) {
        this.displayName = displayName;
    }
}
