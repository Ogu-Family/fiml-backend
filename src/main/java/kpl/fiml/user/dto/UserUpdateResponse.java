package kpl.fiml.user.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class UserUpdateResponse {
    private final Long id;
    private final String name;
    private final String bio;
    private final String profileImage;
    private final String email;
    private final String password;
    private final String contact;

    public static UserUpdateResponse of(Long id, String name, String bio, String profileImage, String email, String password, String contact) {
        return new UserUpdateResponse(id, name, bio, profileImage, email, password, contact);
    }
}
