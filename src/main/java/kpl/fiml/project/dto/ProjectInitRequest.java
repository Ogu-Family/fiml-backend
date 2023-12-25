package kpl.fiml.project.dto;

import kpl.fiml.project.domain.Project;
import kpl.fiml.project.domain.enums.ProjectCategory;
import lombok.Getter;

@Getter
public class ProjectInitRequest {

    private String summary;
    private ProjectCategory category;

    public Project toEntity() {
        return Project.builder()
                .summary(summary)
                .category(category)
                .build();
    }
}
