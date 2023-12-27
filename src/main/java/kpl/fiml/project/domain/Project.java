package kpl.fiml.project.domain;

import jakarta.persistence.*;
import kpl.fiml.global.common.BaseEntity;
import kpl.fiml.project.domain.enums.ProjectCategory;
import kpl.fiml.project.domain.enums.ProjectStatus;
import kpl.fiml.reward.domain.Reward;
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

    private static final int PAYMENT_AT_OFFSET = 1;
    private static final int PAYMENT_END_AT_OFFSET = 7;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // TODO: user_id 참조 외래키 추가

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

    // TODO: 컬럼명 payment_start_at 으로 변경
    @Column(name = "payment_at", columnDefinition = "datetime")
    private LocalDateTime paymentAt;

    @Column(name = "payment_end_at", columnDefinition = "datetime")
    private LocalDateTime paymentEndAt;

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
    public Project(String summary, ProjectCategory category) {
        this.summary = summary;
        this.category = category;

        this.currentAmount = 0L;
        this.sharedCount = 0L;
        this.likedCount = 0L;
        this.sponsorCount = 0L;
        this.status = ProjectStatus.WRITING; // 초기 생성 시 리스트에 노출되지 않고 작성 중 상태로 생성

        this.projectImages = new ArrayList<>();
        this.rewards = new ArrayList<>();
    }

    public void addProjectImage(ProjectImage projectImage) {
        this.projectImages.add(projectImage);
        projectImage.setProject(this);
    }

    public void addReward(Reward reward) {
        this.rewards.add(reward);
        reward.setProject(this);
    }

    public void updateBasicInfo(String summary, ProjectCategory category, String title, List<ProjectImage> projectImages) {
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

    public void updateIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public void updateFundingInfo(Long goalAmount, LocalDateTime startDateTime, LocalDate endDate, Double commissionRate) {
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);
        validateFundingDateTime(startDateTime, endDateTime);

        this.goalAmount = goalAmount;
        this.startAt = startDateTime;
        this.endAt = endDateTime;
        this.commissionRate = commissionRate;

        this.paymentAt = this.endAt.plusDays(PAYMENT_AT_OFFSET);
        this.paymentEndAt = this.endAt.plusDays(PAYMENT_END_AT_OFFSET);
    }

    private void validateFundingDateTime(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        LocalDateTime currentLocalDateTime = LocalDateTime.now();
        if (startDateTime.isAfter(endDateTime)) {
            throw new IllegalArgumentException("펀딩 시작일은 종료일보다 빠를 수 없습니다.");
        }
        if (startDateTime.isBefore(currentLocalDateTime) ||
            endDateTime.isBefore(currentLocalDateTime)) {
            throw new IllegalArgumentException("펀딩 시작일과 종료일은 현재 시간보다 빠를 수 없습니다.");
        }
    }

    public void updateRewards(List<Reward> rewardEntities) {
        this.rewards.clear();
        for (Reward rewardEntity : rewardEntities) {
            addReward(rewardEntity);
        }
    }

    public void submit() {
        validateRequiredFields();
        validateFundingDateTime(this.startAt, this.endAt);
        if (this.status != ProjectStatus.WRITING) {
            throw new IllegalStateException("작성 중인 프로젝트만 승인할 수 있습니다.");
        }

        this.status = ProjectStatus.PREPARING;
    }

    private void validateRequiredFields() {
        if (this.title == null || this.introduction == null || this.summary == null || this.category == null ||
            this.goalAmount == null || this.currentAmount == null || this.startAt == null || this.endAt == null ||
            this.paymentAt == null || this.paymentEndAt == null || this.commissionRate == null ||
            this.status == null || this.sharedCount == null || this.likedCount == null || this.sponsorCount == null ||
            this.projectImages.isEmpty() || this.rewards.isEmpty()) {
            throw new IllegalStateException("프로젝트 정보를 모두 입력해야 합니다.");
        }
    }
}
