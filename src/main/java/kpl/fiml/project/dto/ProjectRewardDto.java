package kpl.fiml.project.dto;

import kpl.fiml.reward.domain.Reward;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class ProjectRewardDto {

    private String title;
    private String content;
    private Long price;
    private Integer quantity;
    private LocalDate deliveryDate;
    private Integer maxPurchaseQuantity;

    public Reward toEntity(int listIndex) {
        return Reward.builder()
                .title(title)
                .content(content)
                .sequence(listIndex)
                .price(price)
                .totalQuantity(quantity)
                .deliveryDate(deliveryDate)
                .maxPurchaseQuantity(maxPurchaseQuantity)
                .build();
    }
}
