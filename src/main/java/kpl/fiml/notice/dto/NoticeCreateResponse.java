package kpl.fiml.notice.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class NoticeCreateResponse {
    private final Long id;
    private final Long userId;

    public static NoticeCreateResponse of(Long id, Long userId) {
        return new NoticeCreateResponse(id, userId);
    }
}
