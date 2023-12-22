package kpl.fiml.project.domain;

import jakarta.persistence.*;
import kpl.fiml.project.domain.enums.ProjectCategory;
import kpl.fiml.project.domain.enums.ProjectStatus;
import kpl.fiml.reward.domain.Reward;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "projects")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // TODO: user_id 참조 외래키 추가

    @Column(name = "title", length = 100)
    private String title;

    @Column(name = "content", columnDefinition = "text")
    private String content;

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

    @Column(name = "payment_at", columnDefinition = "datetime")
    private LocalDateTime paymentAt;

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

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ProjectImage> projectImages;

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Reward> rewards;

    @Builder
    public Project(String summary, ProjectCategory category) {
        this.summary = summary;
        this.category = category;

        this.currentAmount = 0L;
        this.sharedCount = 0L;
        this.likedCount = 0L;
        this.sponsorCount = 0L;
        this.status = ProjectStatus.PREPARING;

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
}
