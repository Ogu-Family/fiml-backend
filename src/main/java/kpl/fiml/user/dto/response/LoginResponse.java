package kpl.fiml.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Schema(description = "로그인 응답 DTO")
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class LoginResponse {
    @Schema(description = "JWT Token", example = "JWT TOKEN")
    private final String jwtToken;
    @Schema(description = "사용자 이름", example = "user name")
    private final String username;
    @Schema(description = "사용자 이메일", example = "test@example.com")
    private final String email;

    public static LoginResponse of(String jwtToken, String username, String email) {
        return new LoginResponse(jwtToken, username, email);
    }
}
