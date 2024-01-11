package kpl.fiml.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "로그인 요청 DTO")
@Getter
@Builder
public class LoginRequest {
    @Schema(description = "사용자 이메일", example = "test@example.com")
    @NotBlank(message = "이메일은 필수 입력값 입니다.")
    private String email;

    @Schema(description = "비밀번호", example = "password123!")
    @NotBlank(message = "비밀번호는 필수 입력값 입니다.")
    private String password;
}
