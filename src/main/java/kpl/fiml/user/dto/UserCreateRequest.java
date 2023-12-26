package kpl.fiml.user.dto;

import kpl.fiml.user.domain.User;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserCreateRequest {
    private String name;
    private String bio;
    private String profileImage;
    private String email;
    private String password;
    private String contact;

    public User toEntity() {
        return User.builder()
                .name(this.name)
                .bio(this.bio)
                .profileImage(this.profileImage)
                .email(this.email)
                .password(this.password)
                .contact(this.contact)
                .build();
    }
}
