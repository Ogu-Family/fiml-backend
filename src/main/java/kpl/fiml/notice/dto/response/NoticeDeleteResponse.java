package kpl.fiml.notice.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class NoticeDeleteResponse {
    private final Long id;
    private final Long userId;
    private final LocalDateTime deletedAt;

    public static NoticeDeleteResponse of(Long id, Long userId, LocalDateTime deletedAt) {
        return new NoticeDeleteResponse(id, userId, deletedAt);
    }
}
