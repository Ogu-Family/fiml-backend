package kpl.fiml.project.dto;

import kpl.fiml.project.domain.Project;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Getter
public class ProjectDto {

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

    private ProjectDto(Project project) {
        this.id = project.getId();
        this.title = project.getTitle();
        this.summary = project.getSummary();
        this.thumbnailImagePath = project.getProjectImages().get(0).getPath();
        this.category = project.getCategory().getDisplayName();
        this.userName = "사용자 이름"; // TODO: 올바른 User 객체를 참조하여 사용자 이름을 가져오기
        this.projectStatus = project.getStatus().getDisplayName();
        this.achieveRate = BigDecimal.valueOf(project.getCurrentAmount())
                .divide(BigDecimal.valueOf(project.getGoalAmount()), 2, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .intValue();
        this.goalAmount = project.getGoalAmount();
        this.currentAmount = project.getCurrentAmount();
        this.remainDay = ChronoUnit.DAYS.between(LocalDateTime.now(), project.getEndAt());
    }

    public static ProjectDto of(Project project) {
        return new ProjectDto(project);
    }
}
