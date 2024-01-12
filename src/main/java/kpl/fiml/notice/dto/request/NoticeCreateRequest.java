package kpl.fiml.notice.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import kpl.fiml.notice.domain.Notice;
import kpl.fiml.project.domain.Project;
import kpl.fiml.user.domain.User;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "공지사항 생성 DTO")
@Getter
@Builder
public class NoticeCreateRequest {

    @Schema(description = "공지사항 내용", example = "notice content")
    @NotBlank(message = "공지사항 내용은 필수 입력 값 입니다.")
    private String content;

    @Schema(description = "프로젝트 id", example = "1")
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
