package kpl.fiml.user.dto;

import lombok.Getter;

@Getter
public class UserUpdateRequest {
    private Long id;  // TODO: Authentication 적용 시 삭제
    private String name;
    private String bio;
    private String profileImage;
    private String email;
    private String password;
    private String contact;
}
