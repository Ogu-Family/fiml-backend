package kpl.fiml.project.dto.request;

import jakarta.validation.constraints.NotNull;
import kpl.fiml.project.domain.Reward;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class ProjectRewardRequest {

    @NotNull
    private String title;
    @NotNull
    private String content;
    @NotNull
    private Long price;
    @NotNull
    private Boolean quantityLimited;
    @NotNull
    private Integer quantity;
    @NotNull
    private LocalDate deliveryDate;
    @NotNull
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
