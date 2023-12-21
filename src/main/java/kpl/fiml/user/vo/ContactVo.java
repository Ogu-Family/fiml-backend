package kpl.fiml.user.vo;

import lombok.Getter;

@Getter
public class ContactVo {
    private String contact;

    public ContactVo(String contact) {
        if (!isValidContact(contact)) {
            throw new IllegalArgumentException("유효하지 않은 전화번호 형식입니다.");
        }
        this.contact = contact;
    }

    private boolean isValidContact(String contact) {
        return contact.matches("\\d+") && !contact.matches(".*[!@#$%^&*()].*");
    }
}
