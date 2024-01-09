package kpl.fiml.project.dto.request;

import jakarta.validation.constraints.NotNull;
import kpl.fiml.project.domain.Project;
import kpl.fiml.project.domain.enums.ProjectCategory;
import kpl.fiml.user.domain.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProjectInitRequest {

    @NotNull
    private String summary;

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
