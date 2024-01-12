package kpl.fiml.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Schema(description = "사용자 삭제 응답 DTO")
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class UserDeleteResponse {
    @Schema(description = "삭제된 사용자 id", example = "1")
    private final Long id;

    public static UserDeleteResponse of(Long id) {
        return new UserDeleteResponse(id);
    }
}
