package kpl.fiml.project.dto;

import kpl.fiml.project.domain.Reward;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class ProjectRewardDto {

    private String title;
    private String content;
    private Long price;
    private Boolean quantityLimited;
    private Integer quantity;
    private LocalDate deliveryDate;
    // TODO: 1 이상의 정수 범위 검증 추가
    private Integer maxPurchaseQuantity;

    public Reward toEntity(int listIndex) {
        return Reward.builder()
                .title(title)
                .content(content)
                .sequence(listIndex)
                .price(price)
                .quantityLimited(quantityLimited)
                .totalQuantity(quantity)
                .deliveryDate(deliveryDate)
                .maxPurchaseQuantity(maxPurchaseQuantity)
                .build();
    }
}
