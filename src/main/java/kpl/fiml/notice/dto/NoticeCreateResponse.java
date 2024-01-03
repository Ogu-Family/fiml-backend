package kpl.fiml.notice.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class NoticeCreateResponse {
    private final Long id;
    private final Long userId;
    private final Long projectId;

    public static NoticeCreateResponse of(Long id, Long userId, Long projectId) {
        return new NoticeCreateResponse(id, userId, projectId);
    }
}
