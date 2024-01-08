package kpl.fiml.user.vo;

import kpl.fiml.user.exception.InvalidPasswordException;
import kpl.fiml.user.exception.UserErrorCode;
import lombok.Getter;

@Getter
public class PasswordVo {

    private String password;
    private static final int PASSWORD_LENGTH = 8;
    private static final String SPECIAL_CHARACTERS_REGEX = ".*[!@#$%^&*()].*";

    public PasswordVo(String password) {
        if (!isValidPassword(password)) {
            throw new InvalidPasswordException(UserErrorCode.INVALID_PASSWORD);
        }
        this.password = password;
    }

    private boolean isValidPassword(String password) {
        return password.length() >= PASSWORD_LENGTH && password.matches(SPECIAL_CHARACTERS_REGEX);
    }
}
