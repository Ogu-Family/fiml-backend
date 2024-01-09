package kpl.fiml.project.domain;

import jakarta.persistence.*;
import kpl.fiml.global.common.BaseEntity;
import kpl.fiml.project.exception.reward.RewardErrorCode;
import kpl.fiml.project.exception.reward.RewardFieldValueException;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Table(name = "rewards")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reward extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @JoinColumn(name = "project_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Project project;

    @Column(name = "title", length = 100, nullable = false)
    private String title;

    @Column(name = "content", columnDefinition = "text")
    private String content;

    @Column(name = "sequence", nullable = false)
    private Integer sequence;

    @Column(name = "price", nullable = false)
    private Long price;

    @Column(name = "quantity_limited", nullable = false)
    private Boolean quantityLimited;

    @Column(name = "remain_quantity", nullable = false)
    private Integer remainQuantity;

    @Column(name = "total_quantity", nullable = false)
    private Integer totalQuantity;

    @Column(name = "delivery_date", columnDefinition = "date", nullable = false)
    private LocalDate deliveryDate;

    @Column(name = "max_purchase_quantity", nullable = false)
    private Integer maxPurchaseQuantity;

    @Builder
    public Reward(String title, String content, Integer sequence, Long price, Boolean quantityLimited, Integer totalQuantity, LocalDate deliveryDate, Integer maxPurchaseQuantity) {
        this.title = title;
        this.content = content;
        this.sequence = sequence;
        this.price = price;
        this.quantityLimited = quantityLimited;
        this.remainQuantity = totalQuantity;
        this.totalQuantity = totalQuantity;
        if (Boolean.FALSE.equals(this.quantityLimited)) {
            this.remainQuantity = Integer.MAX_VALUE;
            this.totalQuantity = Integer.MAX_VALUE;
        }
        this.deliveryDate = deliveryDate;
        this.maxPurchaseQuantity = maxPurchaseQuantity;

        if (this.price < 0) {
            throw new RewardFieldValueException(RewardErrorCode.INVALID_PRICE);
        }

        if (Boolean.TRUE.equals(this.quantityLimited) && this.totalQuantity < 1) {
            throw new RewardFieldValueException(RewardErrorCode.INVALID_QUANTITY_LIMIT);
        }

        if (this.maxPurchaseQuantity < 1) {
            throw new RewardFieldValueException(RewardErrorCode.INVALID_QUANTITY);
        }
    }

    public boolean checkUnderflowPrice(Long amount) {
        return price > amount;
    }
}
