package kpl.fiml.user.dto;

import kpl.fiml.user.domain.User;
import lombok.Getter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
public class UserCreateRequest {
    private String name;
    private String bio;
    private String profileImage;
    private String email;
    private String password;
    private String contact;

    public User toEntity(PasswordEncoder passwordEncoder) {
        return User.builder()
                .name(this.name)
                .bio(this.bio)
                .profileImage(this.profileImage)
                .email(this.email)
                .password(passwordEncoder.encode(this.password))
                .contact(this.contact)
                .build();
    }
}
