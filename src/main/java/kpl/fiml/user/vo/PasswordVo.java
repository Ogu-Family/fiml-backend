package kpl.fiml.user.vo;

import lombok.Getter;

@Getter
public class PasswordVo {

    private String password;
    private static final int PASSWORD_LENGTH = 8;
    private static final String SPECIAL_CHARACTERS_REGEX = ".*[!@#$%^&*()].*";

    public PasswordVo(String password) {
        if (!isValidPassword(password)) {
            throw new IllegalArgumentException("비밀번호는 8자 이상, 특수문자 1가지를 꼭 포함해야 합니다.");
        }
        this.password = password;
    }

    private boolean isValidPassword(String password) {
        return password.length() >= PASSWORD_LENGTH && password.matches(SPECIAL_CHARACTERS_REGEX);
    }
}
