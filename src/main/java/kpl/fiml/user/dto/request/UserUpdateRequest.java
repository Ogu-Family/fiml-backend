package kpl.fiml.user.dto.request;

import lombok.Getter;

@Getter
public class UserUpdateRequest {
    private String name;
    private String bio;
    private String profileImage;
    private String email;
    private String password;
    private String contact;
}
