package kpl.fiml.sponsor.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import kpl.fiml.project.domain.Reward;
import kpl.fiml.sponsor.domain.Sponsor;
import kpl.fiml.user.domain.User;
import lombok.Getter;

@Getter
public class SponsorUpdateRequest {

    @Schema(description = "리워드 아이디")
    @NotNull
    private Long rewardId;

    @Schema(description = "총 후원금", example = "60000L")
    @NotNull
    private Long totalAmount;

    public Sponsor toEntity(User user, Reward reward) {
        return Sponsor.builder()
                .user(user)
                .reward(reward)
                .totalAmount(totalAmount)
                .build();
    }
}
