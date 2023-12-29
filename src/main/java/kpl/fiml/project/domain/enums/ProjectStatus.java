package kpl.fiml.project.domain.enums;

import lombok.Getter;

@Getter
public enum ProjectStatus {

    WRITING("작성 중"),
    PREPARING("준비 중"),
    PROCEEDING("진행 중"),
    FUNDING_COMPLETE("펀딩 완료"),
    FUNDING_FAILURE("펀딩 실패"),
    SETTLEMENT_COMPLETE("정산 완료"),
    CANCEL("취소");

    private final String displayName;

    ProjectStatus(String displayName) {
        this.displayName = displayName;
    }
}
