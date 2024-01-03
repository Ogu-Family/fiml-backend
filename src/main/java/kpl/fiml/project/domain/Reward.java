package kpl.fiml.project.domain;

import jakarta.persistence.*;
import kpl.fiml.global.common.BaseEntity;
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
        this.totalQuantity = totalQuantity;
        this.deliveryDate = deliveryDate;
        this.maxPurchaseQuantity = maxPurchaseQuantity;

        this.remainQuantity = totalQuantity;
    }

    public boolean checkUnderflowPrice(Long amount) {
        return price > amount;
    }
}
