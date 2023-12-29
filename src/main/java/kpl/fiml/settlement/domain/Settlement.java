package kpl.fiml.settlement.domain;

import jakarta.persistence.*;
import kpl.fiml.global.common.BaseEntity;
import kpl.fiml.project.domain.Project;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "settlements")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Settlement extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "project_id")
    private Project project;

    @Column(name = "settle_amount", nullable = false)
    private Long settleAmount;

    @Column(name = "settled_at", columnDefinition = "datetime", nullable = false)
    private LocalDateTime settledAt;

    @Builder
    public Settlement(Project project, Long settleAmount, LocalDateTime settledAt) {
        this.project = project;
        this.settleAmount = settleAmount;
        this.settledAt = settledAt;
    }
}
