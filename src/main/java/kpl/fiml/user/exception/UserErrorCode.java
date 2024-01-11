package kpl.fiml.user.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode {

    INVALID_EMAIL("유효하지 않은 이메일 주소입니다."),
    INVALID_PASSWORD("비밀번호는 8자 이상, 특수문자 1가지를 꼭 포함해야 합니다."),
    EMAIL_NOT_FOUND("가입된 E-MAIL이 아닙니다."),
    DUPLICATED_EMAIL("이미 등록된 이메일 주소 입니다."),
    PASSWORD_MISMATCH("비밀번호가 일치하지 않습니다."),
    ACCESS_DENIED("User 접근 권한이 없습니다."),
    INVALID_AMOUNT("금액은 음수가 될 수 없습니다."),
    CASH_NOT_ENOUGH("보유 현금이 부족합니다."),
    INVALID_CONTACT("전화번호 형식이 유효하지 않습니다.");

    private final String message;

}
