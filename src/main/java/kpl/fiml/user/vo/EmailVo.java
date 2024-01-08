package kpl.fiml.user.vo;

import kpl.fiml.user.exception.InvalidEmailException;
import kpl.fiml.user.exception.UserErrorCode;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class EmailVo {

    private String email;
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    public EmailVo(String email) {
        if (!isValidEmail(email)) {
            throw new InvalidEmailException();
        }
        this.email = email;
    }

    private boolean isValidEmail(String email) {
        return email.matches(EMAIL_REGEX);
    }
}
