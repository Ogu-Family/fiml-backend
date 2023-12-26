package kpl.fiml.user.vo;

import lombok.Builder;
import lombok.Getter;

@Getter
public class EmailVo {

    private String email;
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    public EmailVo(String email) {
        if (!isValidEmail(email)) {
            throw new IllegalArgumentException("유효하지 않은 이메일 주소입니다.");
        }
        this.email = email;
    }

    private boolean isValidEmail(String email) {
        return email.matches(EMAIL_REGEX);
    }
}
