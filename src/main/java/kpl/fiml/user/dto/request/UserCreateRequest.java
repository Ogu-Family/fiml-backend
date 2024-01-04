package kpl.fiml.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import kpl.fiml.user.domain.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserCreateRequest {
    @NotBlank(message = "이름은 필수 입력값 입니다.")
    private String name;

    @NotNull
    private String bio;

    private String profileImage;

    @NotBlank(message = "이메일은 필수 입력값 입니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수 입력값 입니다.")
    private String password;

    @NotBlank(message = "전화번호는 필수 입력값 입니다.")
    private String contact;

    public User toEntity(String encryptPassword) {
        return User.builder()
                .name(this.name)
                .bio(this.bio)
                .profileImage(this.profileImage)
                .email(this.email)
                .encryptPassword(encryptPassword)
                .contact(this.contact)
                .build();
    }
}
