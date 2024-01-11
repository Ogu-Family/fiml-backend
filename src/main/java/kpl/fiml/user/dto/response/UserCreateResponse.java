package kpl.fiml.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Schema(description = "회원가입 응답 DTO")
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class UserCreateResponse {
    @Schema(description = "생성된 사용자 id", example = "1")
    private final Long userId;

    public static UserCreateResponse of(Long userId) {
        return new UserCreateResponse(userId);
    }
}
