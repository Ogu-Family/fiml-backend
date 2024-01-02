package kpl.fiml.notice.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class NoticeUpdateResponse {
    private final Long id;
    private final String content;
    private final Long userId;

    public static NoticeUpdateResponse of(Long id, String content, Long userId) {
        return new NoticeUpdateResponse(id, content, userId);
    }
}
