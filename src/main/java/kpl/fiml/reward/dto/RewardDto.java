package kpl.fiml.reward.dto;

import kpl.fiml.reward.domain.Reward;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class RewardDto {

    private String title;
    private String content;
    private Integer sequence;
    private Long price;
    private Integer quantity;
    private LocalDate deliveryDate;
    private Integer maxPurchaseQuantity;

    public Reward toEntity() {
        return Reward.builder()
                .title(title)
                .content(content)
                .sequence(sequence)
                .price(price)
                .totalQuantity(quantity)
                .deliveryDate(deliveryDate)
                .maxPurchaseQuantity(maxPurchaseQuantity)
                .build();
    }
}
