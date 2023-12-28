package kpl.fiml.user.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class LoginResponse {
    private final String jwtToken;
    private final String username;
    private final String email;

    public static LoginResponse of(String jwtToken, String username, String email) {
        return new LoginResponse(jwtToken, username, email);
    }
}
