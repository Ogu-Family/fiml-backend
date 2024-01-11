package kpl.fiml.project.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import kpl.fiml.project.domain.Project;
import kpl.fiml.project.domain.enums.ProjectCategory;
import kpl.fiml.user.domain.User;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "프로젝트 초기 생성 정보")
@Getter
@Builder
public class ProjectInitRequest {

    @Schema(description = "프로젝트 요약", example = "프로젝트 요약")
    @NotNull
    private String summary;

    @Schema(description = "프로젝트 카테고리", example = "DIGITAL_GAME")
    @NotNull
    private ProjectCategory category;

    public Project toEntity(User user) {
        return Project.builder()
                .summary(summary)
                .category(category)
                .user(user)
                .build();
    }
}
