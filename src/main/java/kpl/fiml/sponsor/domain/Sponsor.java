package kpl.fiml.sponsor.domain;

import jakarta.persistence.*;
import kpl.fiml.global.common.BaseEntity;
import kpl.fiml.project.domain.Reward;
import kpl.fiml.user.domain.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "sponsors")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Sponsor extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "reward_id")
    private Reward reward;

    @Column(name = "total_amount", nullable = false)
    private Long totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private SponsorStatus status;

    @Builder
    public Sponsor(User user, Reward reward, Long totalAmount) {
        this.user = user;
        this.reward = reward;
        this.totalAmount = totalAmount;

        this.status = SponsorStatus.FUNDING_PROCEEDING;
    }

    public void updateStatus(SponsorStatus status) {
        this.status = status;
    }
}
