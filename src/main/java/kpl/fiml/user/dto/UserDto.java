package kpl.fiml.user.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class UserDto {
    private final Long id;
    private final String name;
    private final String bio;
    private final String profileImage;
    private final String email;
    private final String contact;
    private final Long cash;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static UserDto of(Long id, String name, String bio, String profileImage, String email, String contact, Long cash,
                             LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new UserDto(id, name, bio, profileImage, email, contact, cash, createdAt, updatedAt);
    }
}
