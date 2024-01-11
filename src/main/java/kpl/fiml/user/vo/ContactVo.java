package kpl.fiml.user.vo;

import kpl.fiml.user.exception.InvalidContactException;
import kpl.fiml.user.exception.UserErrorCode;
import lombok.Getter;

@Getter
public class ContactVo {
    private String contact;
    private static final String SPECIAL_CHARACTERS_REGEX = ".*[!@#$%^&*()].*";
    private static final String DIGITS_ONLY_REGEX = "\\d+";

    public ContactVo(String contact) {
        if (!isValidContact(contact)) {
            throw new InvalidContactException(UserErrorCode.INVALID_CONTACT);
        }
        this.contact = contact;
    }

    private boolean isValidContact(String contact) {
        return contact.matches(DIGITS_ONLY_REGEX) && !contact.matches(SPECIAL_CHARACTERS_REGEX);
    }
}
