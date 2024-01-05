package kpl.fiml.user.vo;

import kpl.fiml.user.exception.InvalidEmailException;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class EmailVo {

    private String email;
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    public EmailVo(String email) {
        if (!isValidEmail(email)) {
            throw new InvalidEmailException("유효하지 않은 이메일 주소입니다.", "INVALID_EMAIL");
        }
        this.email = email;
    }

    private boolean isValidEmail(String email) {
        return email.matches(EMAIL_REGEX);
    }
}
