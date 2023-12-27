package kpl.fiml.notice.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class NoticeDeleteResponse {
    private final Long id;
    private final LocalDateTime deletedAt;

    public static NoticeDeleteResponse of(Long id, LocalDateTime deletedAt) {
        return new NoticeDeleteResponse(id, deletedAt);
    }
}
