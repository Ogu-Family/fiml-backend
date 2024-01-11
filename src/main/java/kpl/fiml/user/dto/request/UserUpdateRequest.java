package kpl.fiml.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Schema(description = "사용자 정보 수정 요청 DTO")
@Getter
public class UserUpdateRequest {
    @Schema(description = "사용자 이름", example = "updated user name")
    @NotBlank(message = "이름은 필수 입력값입니다.")
    private String name;

    @Schema(description = "사용자 소개말", example = "Update Hello!")
    private String bio;

    @Schema(description = "사용자 프로필 이미지", example = "updateImage.jpeg")
    private String profileImage;

    @Schema(description = "사용자 이메일", example = "update@example.com")
    @NotBlank(message = "이메일은 필수 입력값입니다.")
    private String email;

    @Schema(description = "사용자 비밀번호", example = "updatePassword123!")
    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    private String password;

    @Schema(description = "사용자 연락처", example = "01012345678")
    @NotBlank(message = "전화번호는 필수 입력값입니다.")
    private String contact;
}
