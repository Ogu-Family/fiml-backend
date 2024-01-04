package kpl.fiml.project.dto;

import kpl.fiml.project.domain.Project;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ProjectDetailResponse {

    private final Long id;
    private final String title;
    private final String summary;
    private final String introduction;
    private final String category;
    private final String userName;
    private final String projectStatus;
    private final Integer achieveRate;
    private final Long goalAmount;
    private final Long currentAmount;
    private final Long likedCount;
    private final Long sponsorCount;
    private final Long sharedCount;
    private final Long remainValueToEnd;
    private final String remainValueToEndUnit;
    private final LocalDate startAt;
    private final LocalDate endAt;
    private final LocalDate paymentAt;
    private final List<ProjectImageResponse> projectImages;
    private final List<ProjectRewardResponse> projectRewards;

    public static ProjectDetailResponse of(Project project) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        ChronoUnit chronoUnit = getChronoUnit(project.getEndAt(), currentDateTime);
        long timeRemaining = chronoUnit.between(currentDateTime, project.getEndAt());

        return new ProjectDetailResponse(
                project.getId(),
                project.getTitle(),
                project.getSummary(),
                project.getIntroduction(),
                project.getCategory().getDisplayName(),
                project.getUser().getName(),
                project.getStatus().getDisplayName(),
                BigDecimal.valueOf(project.getCurrentAmount())
                        .divide(BigDecimal.valueOf(project.getGoalAmount()), 2, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100))
                        .intValue(),
                project.getGoalAmount(),
                project.getCurrentAmount(),
                project.getLikedCount(),
                project.getSponsorCount(),
                project.getSharedCount(),
                timeRemaining,
                chronoUnit.name(),
                project.getStartAt().toLocalDate(),
                project.getEndAt().toLocalDate(),
                project.getEndAt().toLocalDate().plusDays(1),
                project.getProjectImages().stream()
                        .map(ProjectImageResponse::of)
                        .toList(),
                project.getRewards().stream()
                        .map(ProjectRewardResponse::of)
                        .toList()
        );
    }

    private static ChronoUnit getChronoUnit(LocalDateTime endAt, LocalDateTime now) {
        if (ChronoUnit.DAYS.between(now, endAt) > 0) {
            return ChronoUnit.DAYS;
        } else if (ChronoUnit.HOURS.between(now, endAt) > 0) {
            return ChronoUnit.HOURS;
        } else if (ChronoUnit.MINUTES.between(now, endAt) > 0) {
            return ChronoUnit.MINUTES;
        } else {
            return ChronoUnit.SECONDS;
        }
    }
}
