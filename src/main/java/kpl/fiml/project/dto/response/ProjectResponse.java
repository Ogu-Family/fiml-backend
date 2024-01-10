package kpl.fiml.project.dto.response;

import kpl.fiml.project.domain.Project;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Getter
public class ProjectResponse {

    private final Long id;
    private final String title;
    private final String summary;
    private final String thumbnailImagePath;
    private final String category;
    private final String userName;
    private final String projectStatus;
    private final Integer achieveRate;
    private final Long goalAmount;
    private final Long currentAmount;
    private final Long remainDay;

    private ProjectResponse(Project project) {
        this.id = project.getId();
        this.title = project.getTitle();
        this.summary = project.getSummary();
        this.thumbnailImagePath = project.getProjectImages().isEmpty()
                ? null
                : project.getProjectImages().get(0).getPath();
        this.category = project.getCategory().getDisplayName();
        this.userName = project.getUser().getName();
        this.projectStatus = project.getStatus().getDisplayName();
        this.achieveRate = BigDecimal.valueOf(project.getCurrentAmount())
                .divide(BigDecimal.valueOf(project.getGoalAmount()), 2, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .intValue();
        this.goalAmount = project.getGoalAmount();
        this.currentAmount = project.getCurrentAmount();
        this.remainDay = ChronoUnit.DAYS.between(LocalDateTime.now(), project.getEndAt());
    }

    public static ProjectResponse of(Project project) {
        return new ProjectResponse(project);
    }
}
