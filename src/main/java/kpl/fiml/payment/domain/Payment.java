package kpl.fiml.payment.domain;

import jakarta.persistence.*;
import kpl.fiml.global.common.BaseEntity;
import kpl.fiml.sponsor.domain.Sponsor;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "payments")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sponsor_id")
    private Sponsor sponsor;

    @Column(name = "is_payed", nullable = false)
    private Boolean isPayed;

    @Column(name = "requested_at", columnDefinition = "datetime", nullable = false)
    private LocalDateTime requestedAt;

    @Column(name = "approved_at", columnDefinition = "datetime")
    private LocalDateTime approvedAt;

    @Builder
    public Payment(Sponsor sponsor, Boolean isPayed, LocalDateTime requestedAt) {
        this.sponsor = sponsor;
        this.isPayed = isPayed;
        this.requestedAt = requestedAt;
    }

    public void updateApprovedAt(LocalDateTime approvedAt) {
        this.approvedAt = approvedAt;
    }
}
