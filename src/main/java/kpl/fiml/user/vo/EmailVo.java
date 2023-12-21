package kpl.fiml.user.vo;

import lombok.Builder;
import lombok.Getter;

@Getter
public class EmailVo {

    private String email;

    public EmailVo(String email) {
        // 여기에서 이메일 유효성 검증을 수행합니다.
        if (!isValidEmail(email)) {
            throw new IllegalArgumentException("유효하지 않은 이메일 주소입니다.");
        }
        this.email = email;
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }
}
