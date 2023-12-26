package kpl.fiml.user.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class UserCreateResponse {
    private final Long userId;

    public static UserCreateResponse of(Long userId) {
        return new UserCreateResponse(userId);
    }
}
