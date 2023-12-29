package kpl.fiml.project.dto;

import kpl.fiml.project.domain.Project;
import lombok.Getter;

// TODO: 필요한 값 추가
@Getter
public class ProjectDto {

    private final Long id;
    private final String title;
    private final String summary;
    private final Long goalAmount;
    private final Long currentAmount;

    private ProjectDto(Project project) {
        this.id = project.getId();
        this.title = project.getTitle();
        this.summary = project.getSummary();
        this.goalAmount = project.getGoalAmount();
        this.currentAmount = project.getCurrentAmount();
    }

    public static ProjectDto of(Project project) {
        return new ProjectDto(project);
    }
}
