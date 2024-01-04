package kpl.fiml.project.dto;

import kpl.fiml.project.domain.Reward;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ProjectRewardResponse {

    private final Long id;
    private final String content;
    private final Integer sequence;
    private final Long price;
    private final Boolean quantityLimited;
    private final Integer selectedQuantity;
    private final Integer totalQuantity;
    private final LocalDate deliveryDate;
    private final Integer maxPurchaseQuantity;

    public static ProjectRewardResponse of(Reward reward) {
        return new ProjectRewardResponse(
                reward.getId(),
                reward.getContent(),
                reward.getSequence(),
                reward.getPrice(),
                reward.getQuantityLimited(),
                reward.getTotalQuantity() - reward.getRemainQuantity(),
                reward.getTotalQuantity(),
                reward.getDeliveryDate(),
                reward.getMaxPurchaseQuantity()
        );
    }
}
