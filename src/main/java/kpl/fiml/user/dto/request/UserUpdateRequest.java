package kpl.fiml.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UserUpdateRequest {
    @NotBlank(message = "이름은 필수 입력값입니다.")
    private String name;

    private String bio;

    private String profileImage;

    @NotBlank(message = "이메일은 필수 입력값입니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    private String password;

    @NotBlank(message = "전화번호는 필수 입력값입니다.")
    private String contact;
}
