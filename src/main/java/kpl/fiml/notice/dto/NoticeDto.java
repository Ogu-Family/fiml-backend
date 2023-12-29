package kpl.fiml.notice.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class NoticeDto {
    private final Long id;
    private final String content;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static NoticeDto of(Long id, String content, LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new NoticeDto(id, content, createdAt, updatedAt);
    }
}
