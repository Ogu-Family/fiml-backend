package kpl.fiml.reward.domain;

import jakarta.persistence.*;
import kpl.fiml.project.domain.Project;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Table(name = "rewards")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reward {

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

    @Column(name = "price", nullable = false)
    private Long price;

    @Column(name = "remain_quantity", nullable = false)
    private Integer remainQuantity;

    @Column(name = "total_quantity", nullable = false)
    private Integer totalQuantity;

    @Column(name = "delivery_date", columnDefinition = "date", nullable = false)
    private LocalDate deliveryDate;

    @Column(name = "max_purchase_quantity", nullable = false)
    private Integer maxPurchaseQuantity;

    @Builder
    public Reward(String title, String content, Long price, Integer totalQuantity, LocalDate deliveryDate, Integer maxPurchaseQuantity) {
        this.title = title;
        this.content = content;
        this.price = price;
        this.totalQuantity = totalQuantity;
        this.deliveryDate = deliveryDate;
        this.maxPurchaseQuantity = maxPurchaseQuantity;

        this.remainQuantity = totalQuantity;
    }
}
