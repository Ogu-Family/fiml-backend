package kpl.fiml.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import kpl.fiml.user.domain.User;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "사용자 회원가입 요청 DTO")
@Getter
@Builder
public class UserCreateRequest {
    @Schema(description = "사용자 이름", example = "user name")
    @NotBlank(message = "이름은 필수 입력값 입니다.")
    private String name;

    @Schema(description = "사용자 소개말", example = "Hello!")
    @NotNull
    private String bio;

    @Schema(description = "사용자 프로필 이미지", example = "image.jpeg")
    private String profileImage;

    @Schema(description = "사용자 이메일", example = "test@example.com")
    @NotBlank(message = "이메일은 필수 입력값 입니다.")
    private String email;

    @Schema(description = "사용자 비밀번호", example = "password123!")
    @NotBlank(message = "비밀번호는 필수 입력값 입니다.")
    private String password;

    @Schema(description = "사용자 연락처", example = "01012345678")
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
