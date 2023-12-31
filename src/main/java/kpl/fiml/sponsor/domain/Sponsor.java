package kpl.fiml.sponsor.domain;

import jakarta.persistence.*;
import kpl.fiml.global.common.BaseEntity;
import kpl.fiml.project.domain.Reward;
import kpl.fiml.sponsor.exception.InvalidTotalAmountException;
import kpl.fiml.sponsor.exception.SponsorErrorCode;
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
        validateTotalAmount(reward, totalAmount);

        this.user = user;
        this.reward = reward;
        this.totalAmount = totalAmount;

        this.status = SponsorStatus.FUNDING_PROCEEDING;
    }

    public void updateRewardAndTotalAmount(Reward reward, Long totalAmount) {
        validateTotalAmount(reward, totalAmount);

        this.reward = reward;
        this.totalAmount = totalAmount;
    }

    public void updateStatusToPaymentProceeding() {
        this.status = SponsorStatus.PAYMENT_PROCEEDING;
    }

    public void updateStatusToFundingFail() {
        this.status = SponsorStatus.FUNDING_FAIL;
    }

    public void updateStatusToComplete() {
        this.status = SponsorStatus.COMPLETE;
    }

    public void paymentFail() {
        this.status = SponsorStatus.PAYMENT_FAIL;
        this.reward.getProject().updateSponsorDeleteInfo(this.totalAmount);
    }

    public void deleteSponsor() {
        delete();
    }

    private void validateTotalAmount(Reward reward, Long totalAmount) {
        if (reward.checkUnderflowPrice(totalAmount)) {
            throw new InvalidTotalAmountException(SponsorErrorCode.INVALID_TOTAL_AMOUNT);
        }
    }
}
