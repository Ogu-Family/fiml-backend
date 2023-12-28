package kpl.fiml.notice.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class NoticeCreateResponse {
    private final Long id;

    public static NoticeCreateResponse of(Long id) {
        return new NoticeCreateResponse(id);
    }
}
