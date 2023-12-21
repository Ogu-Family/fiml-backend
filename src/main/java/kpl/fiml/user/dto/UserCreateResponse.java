package kpl.fiml.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserCreateResponse {
    Long userId;

    @Builder
    public UserCreateResponse(Long userId) {
        this.userId = userId;
    }
}
