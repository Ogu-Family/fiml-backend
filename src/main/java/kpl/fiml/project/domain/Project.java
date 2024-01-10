package kpl.fiml.project.domain;

import jakarta.persistence.*;
import kpl.fiml.global.common.BaseEntity;
import kpl.fiml.project.domain.enums.ProjectCategory;
import kpl.fiml.project.domain.enums.ProjectStatus;
import kpl.fiml.project.exception.project.*;
import kpl.fiml.user.domain.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "projects")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Project extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "title", length = 100)
    private String title;

    @Column(name = "introduction", columnDefinition = "text")
    private String introduction;

    @Column(name = "summary", length = 200, nullable = false)
    private String summary;

    @Column(name = "goal_amount")
    private Long goalAmount;

    @Column(name = "current_amount", nullable = false)
    private Long currentAmount;

    @Column(name = "start_at", columnDefinition = "datetime")
    private LocalDateTime startAt;

    @Column(name = "end_at", columnDefinition = "datetime")
    private LocalDateTime endAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private ProjectCategory category;

    @Column(name = "commission_rate")
    private Double commissionRate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ProjectStatus status;

    @Column(name = "shared_count", nullable = false)
    private Long sharedCount;

    @Column(name = "liked_count", nullable = false)
    private Long likedCount;

    @Column(name = "sponsor_count", nullable = false)
    private Long sponsorCount;

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProjectImage> projectImages;

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reward> rewards;

    @Builder
    public Project(String summary, ProjectCategory category, User user) {
        this.summary = summary;
        this.category = category;
        this.user = user;

        this.currentAmount = 0L;
        this.sharedCount = 0L;
        this.likedCount = 0L;
        this.sponsorCount = 0L;
        this.status = ProjectStatus.WRITING; // 초기 생성 시 리스트에 노출되지 않고 작성 중 상태로 생성

        this.projectImages = new ArrayList<>();
        this.rewards = new ArrayList<>();
    }

    private void addProjectImage(ProjectImage projectImage) {
        this.projectImages.add(projectImage);
        projectImage.setProject(this);
    }

    private void addReward(Reward reward) {
        this.rewards.add(reward);
        reward.setProject(this);
    }

    public void updateBasicInfo(String summary, ProjectCategory category, String title, List<ProjectImage> projectImages, User user) {
        validateUser(user);

        this.summary = summary;
        this.category = category;
        this.title = title;

        updateImages(projectImages);
    }

    private void updateImages(List<ProjectImage> projectImages) {
        this.projectImages.clear();
        for (ProjectImage projectImageDto : projectImages) {
            addProjectImage(projectImageDto);
        }
    }

    public void updateIntroduction(String introduction, User user) {
        validateUser(user);

        this.introduction = introduction;
    }

    public void updateFundingInfo(Long goalAmount, LocalDateTime startDateTime, LocalDate endDate, Double commissionRate, User user) {
        validateUser(user);

        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);
        validateFundingDateTime(startDateTime, endDateTime);

        this.goalAmount = goalAmount;
        this.startAt = startDateTime;
        this.endAt = endDateTime;
        this.commissionRate = commissionRate;

        if (this.goalAmount < 0) {
            throw new ProjectFieldValueException(ProjectErrorCode.INVALID_PROJECT_TARGET_AMOUNT);
        }

        if (this.commissionRate < 0 || this.commissionRate > 100) {
            throw new ProjectFieldValueException(ProjectErrorCode.INVALID_PROJECT_COMMISSION_RATE);
        }
    }

    private void validateFundingDateTime(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        LocalDateTime currentLocalDateTime = LocalDateTime.now();
        if (startDateTime.isAfter(endDateTime)) {
            throw new ProjectFieldValueException(ProjectErrorCode.PROJECT_FUNDING_START_DATE_EARLIER_THAN_END_DATE);
        }
        if (startDateTime.isBefore(currentLocalDateTime) ||
            endDateTime.isBefore(currentLocalDateTime)) {
            throw new ProjectFieldValueException(ProjectErrorCode.PROJECT_FUNDING_DATE_EARLIER_THAN_NOW);
        }
    }

    public void updateRewards(List<Reward> rewardEntities, User user) {
        validateUser(user);

        this.rewards.clear();
        for (Reward rewardEntity : rewardEntities) {
            addReward(rewardEntity);
        }
    }

    public void submit(User user) {
        validateUser(user);
        validateRequiredFields();
        validateFundingDateTime(this.startAt, this.endAt);
        if (this.status != ProjectStatus.WRITING) {
            throw new ProjectSubmitException(ProjectErrorCode.ONLY_WRITING_PROJECT_CAN_SUBMIT);
        }

        this.status = ProjectStatus.PREPARING;
    }

    private void validateRequiredFields() {
        if (this.title == null || this.introduction == null || this.summary == null || this.category == null ||
            this.goalAmount == null || this.currentAmount == null || this.startAt == null || this.endAt == null ||
            this.commissionRate == null || this.status == null || this.sharedCount == null || this.likedCount == null ||
            this.sponsorCount == null || this.projectImages.isEmpty() || this.rewards.isEmpty()) {
            throw new ProjectSubmitException(ProjectErrorCode.PROJECT_INFO_NOT_FILLED);
        }
    }

    public void updateSponsorAddInfo(Long paymentAddAmount) {
        if (this.status != ProjectStatus.PROCEEDING) {
            throw new ProjectFundingException(ProjectErrorCode.NOT_PROCEEDING_PROJECT);
        }

        if (paymentAddAmount <= 0) {
            throw new ProjectFieldValueException(ProjectErrorCode.INVALID_PAYMENT_AMOUNT);
        }

        this.currentAmount += paymentAddAmount;
        this.sponsorCount++;
    }

    public void updateSponsorDeleteInfo(Long paymentFailAmount) {
        if (paymentFailAmount <= 0) {
            throw new ProjectFieldValueException(ProjectErrorCode.INVALID_CANCEL_AMOUNT);
        }

        this.currentAmount -= paymentFailAmount;
        this.sponsorCount--;
    }

    public void updateCurrentAmount(Long amount) {
        this.currentAmount += amount;
    }

    public void updateStatusToSettlementComplete() {
        this.status = ProjectStatus.SETTLEMENT_COMPLETE;
    }

    private void validateUser(User user) {
        if (!this.user.isSameUser(user)) {
            throw new ProjectAccessException(ProjectErrorCode.ONLY_PROJECT_OWNER_CAN_ACCESS);
        }
    }

    public void deleteProject(User user) {
        validateUser(user);
        checkDeletionConditions();

        this.status = ProjectStatus.CANCEL;
        this.delete();
    }

    private void checkDeletionConditions() {
        if (this.status == ProjectStatus.PROCEEDING && this.sponsorCount > 0) {
            throw new ProjectDeleteException(ProjectErrorCode.CANNOT_DELETE_PROJECT_WITH_SPONSORS);
        }
        if (this.status == ProjectStatus.FUNDING_COMPLETE || this.status == ProjectStatus.FUNDING_FAILURE || this.status == ProjectStatus.SETTLEMENT_COMPLETE) {
            throw new ProjectDeleteException(ProjectErrorCode.CANNOT_DELETE_PROJECT_AFTER_END_DATE);
        }
    }
}
