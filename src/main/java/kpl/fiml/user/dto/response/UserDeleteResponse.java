package kpl.fiml.user.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class UserDeleteResponse {
    private final Long id;

    public static UserDeleteResponse of(Long id) {
        return new UserDeleteResponse(id);
    }
}
