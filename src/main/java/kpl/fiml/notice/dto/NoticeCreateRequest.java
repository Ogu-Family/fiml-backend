package kpl.fiml.notice.dto;

import kpl.fiml.notice.domain.Notice;
import kpl.fiml.user.domain.User;
import lombok.Getter;

@Getter
public class NoticeCreateRequest {
    private String content;
    private Long userId;

    public Notice toEntity(User user) {
        return Notice.builder()
                .content(content)
                .user(user)
                .build();
    }
}
