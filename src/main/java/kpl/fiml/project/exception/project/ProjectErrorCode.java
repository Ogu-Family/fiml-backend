package kpl.fiml.project.exception.project;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProjectErrorCode {

    PROJECT_NOT_FOUND("프로젝트 정보가 존재하지 않습니다."),
    ALREADY_LIKED_PROJECT("이미 좋아요를 누른 프로젝트입니다."),
    NOT_LIKED_PROJECT("좋아요를 누르지 않은 프로젝트입니다."),
    INVALID_FEE_RATE("수수료율은 0-100 사이의 실수여야 합니다."),
    INVALID_TARGET_AMOUNT("목표 금액은 0 이상의 정수여야 합니다."),
    CANNOT_DELETE_PROJECT_AFTER_END_DATE("펀딩 기간이 종료된 프로젝트는 삭제할 수 없습니다."),
    CANNOT_DELETE_PROJECT_WITH_SPONSORS("후원자가 있는 프로젝트는 삭제할 수 없습니다."),
    ONLY_PROJECT_OWNER_CAN_ACCESS("프로젝트 작성자만 접근할 수 있습니다."),
    INVALID_CANCEL_AMOUNT("결제 취소 금액은 1원 이상의 정수여야 합니다."),
    INVALID_PAYMENT_AMOUNT("결제 금액은 1원 이상의 정수여야 합니다."),
    NOT_PROCEEDING_PROJECT("진행 중인 프로젝트가 아닙니다."),
    PROJECT_INFO_NOT_FILLED("프로젝트 정보를 모두 입력해야 합니다."),
    ONLY_WRITING_PROJECT_CAN_SUBMIT("작성 중인 프로젝트만 승인할 수 있습니다."),
    PROJECT_FUNDING_DATE_EARLIER_THAN_NOW("펀딩 시작일과 종료일은 현재 시간보다 빠를 수 없습니다."),
    PROJECT_FUNDING_START_DATE_EARLIER_THAN_END_DATE("펀딩 시작일은 종료일보다 빠를 수 없습니다."),
    INVALID_PROJECT_TARGET_AMOUNT("목표 금액은 0 이상의 정수여야 합니다."),
    INVALID_PROJECT_COMMISSION_RATE("수수료율은 0-100 사이의 실수여야 합니다."),
    INVALID_PROJECT_ACHIEVE_RATE_RANGE("최소 달성률은 최대 달성률보다 작거나 같아야 합니다."),
    INVALID_PROJECT_ACHIEVE_RATE("달성률은 0 - 100 사이의 정수만 가능합니다.");

    private final String message;
}
