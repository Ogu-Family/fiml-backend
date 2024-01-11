package kpl.fiml.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Schema(description = "사용자 상세 정보 DTO")
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class UserDto {
    @Schema(description = "사용자 id", example = "1")
    private final Long id;
    @Schema(description = "사용자 이름", example = "user name")
    private final String name;
    @Schema(description = "사용자 소개말", example = "Hello!")
    private final String bio;
    @Schema(description = "사용자 프로필 이미지", example = "image.jpeg")
    private final String profileImage;
    @Schema(description = "사용자 이메일", example = "test@example.com")
    private final String email;
    @Schema(description = "사용자 연락처", example = "01012345678")
    private final String contact;
    @Schema(description = "사용자 캐시", example = "0")
    private final Long cash;
    @Schema(description = "사용자 가입 일시")
    private final LocalDateTime createdAt;
    @Schema(description = "사용자 정보 수정 일시")
    private final LocalDateTime updatedAt;

    public static UserDto of(Long id, String name, String bio, String profileImage, String email, String contact, Long cash,
                             LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new UserDto(id, name, bio, profileImage, email, contact, cash, createdAt, updatedAt);
    }
}
