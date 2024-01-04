package kpl.fiml.notice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import kpl.fiml.notice.domain.Notice;
import kpl.fiml.project.domain.Project;
import kpl.fiml.user.domain.User;
import lombok.Getter;

@Getter
public class NoticeCreateRequest {

    @NotBlank(message = "공지사항 내용은 필수 입력 값 입니다.")
    private String content;

    @NotNull(message = "projectId 값이 Null 입니다.")
    private Long projectId;

    public Notice toEntity(User user, Project project) {
        return Notice.builder()
                .content(content)
                .user(user)
                .project(project)
                .build();
    }
}
