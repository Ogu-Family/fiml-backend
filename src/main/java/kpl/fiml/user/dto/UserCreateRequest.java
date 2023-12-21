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

    @Builder
    public UserCreateRequest(String name, String bio, String profileImage, String email, String password, String contact) {
        this.name = name;
        this.bio = bio;
        this.profileImage = profileImage;
        this.email = email;
        this.password = password;
        this.contact = contact;
    }

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
