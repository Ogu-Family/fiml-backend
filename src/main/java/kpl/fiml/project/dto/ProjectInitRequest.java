package kpl.fiml.project.dto;

import kpl.fiml.project.domain.Project;
import kpl.fiml.project.domain.enums.ProjectCategory;
import kpl.fiml.user.domain.User;
import lombok.Getter;

@Getter
public class ProjectInitRequest {

    private String summary;
    private ProjectCategory category;

    public Project toEntity(User user) {
        return Project.builder()
                .summary(summary)
                .category(category)
                .user(user)
                .build();
    }
}
