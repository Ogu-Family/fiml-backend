package kpl.fiml.user.vo;

import lombok.Getter;

@Getter
public class PasswordVo {

    private String password;

    public PasswordVo(String password) {
        if (!isValidPassword(password)) {
            throw new IllegalArgumentException("비밀번호는 8자 이상, 특수문자 1가지를 꼭 포함해야 합니다.");
        }
        this.password = password;
    }

    private boolean isValidPassword(String password) {
        return password.length() >= 8 && password.matches(".*[!@#$%^&*()].*");
    }
}
