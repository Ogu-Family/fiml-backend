package kpl.fiml.sponsor.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SponsorErrorCode {

    SPONSOR_NOT_FOUND("후원 정보가 존재하지 않습니다."),
    INVALID_TOTAL_AMOUNT("후원 금액이 리워드 가격보다 적습니다."),
    SPONSOR_ACCESS_DENIED("접근 가능한 후원 정보가 아닙니다."),
    SPONSOR_MODIFY_DENIED("펀딩 종료 후에는 후원 수정 및 삭제가 불가합니다.");

    private final String message;
}
