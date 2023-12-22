package kpl.fiml.project.domain.enums;

import lombok.Getter;

@Getter
public enum ProjectStatus {

    WRITING("작성 중"),
    PREPARING("준비 중"),
    PROCEEDING("진행 중"),
    COMPLETE("완료"),
    CANCEL("취소");

    private final String displayName;

    ProjectStatus(String displayName) {
        this.displayName = displayName;
    }
}
