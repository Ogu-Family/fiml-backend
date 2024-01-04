package kpl.fiml.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class LoginRequest {
    @NotBlank(message = "이메일은 필수 입력값 입니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수 입력값 입니다.")
    private String password;
}
