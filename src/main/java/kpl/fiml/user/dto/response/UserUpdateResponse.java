package kpl.fiml.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Schema(description = "사용자 정보 수정 응답 DTO")
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class UserUpdateResponse {
    @Schema(description = "수정된 사용자 id", example = "1")
    private final Long id;

    @Schema(description = "사용자 이름", example = "updated user name")
    private final String name;

    @Schema(description = "사용자 소개말", example = "Update Hello!")
    private final String bio;

    @Schema(description = "사용자 프로필 이미지", example = "updateImage.jpeg")
    private final String profileImage;

    @Schema(description = "사용자 이메일", example = "update@example.com")
    private final String email;

    @Schema(description = "사용자 비밀번호", example = "updatePassword123!")
    private final String password;

    @Schema(description = "사용자 연락처", example = "01012345678")
    private final String contact;

    public static UserUpdateResponse of(Long id, String name, String bio, String profileImage, String email, String password, String contact) {
        return new UserUpdateResponse(id, name, bio, profileImage, email, password, contact);
    }
}
