package kpl.fiml.project.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import kpl.fiml.project.domain.Reward;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Schema(description = "프로젝트 리워드 생성 정보")
@Getter
@Builder
public class ProjectRewardRequest {

    @Schema(description = "프로젝트 리워드 제목", example = "프로젝트 리워드 제목")
    @NotNull
    private String title;

    @Schema(description = "프로젝트 리워드 내용", example = "프로젝트 리워드 내용")
    @NotNull
    private String content;

    @Schema(description = "프로젝트 리워드 가격", example = "10000")
    @NotNull
    private Long price;

    @Schema(description = "프로젝트 리워드 수량 제한 여부", example = "true")
    @NotNull
    private Boolean quantityLimited;

    @Schema(description = "프로젝트 리워드 수량", example = "100")
    @NotNull
    private Integer quantity;

    @Schema(description = "프로젝트 리워드 배송 날짜", example = "2099-12-31")
    @NotNull
    private LocalDate deliveryDate;

    @Schema(description = "프로젝트 리워드 최대 구매 수량", example = "10")
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
