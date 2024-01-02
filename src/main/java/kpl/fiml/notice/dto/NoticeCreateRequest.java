package kpl.fiml.notice.dto;

import kpl.fiml.notice.domain.Notice;
import kpl.fiml.project.domain.Project;
import kpl.fiml.user.domain.User;
import lombok.Getter;

@Getter
public class NoticeCreateRequest {
    private String content;
    private Long userId; // TODO : authentication 적용시 삭제
    private Long projectId;

    public Notice toEntity(User user, Project project) {
        return Notice.builder()
                .content(content)
                .user(user)
                .project(project)
                .build();
    }
}
