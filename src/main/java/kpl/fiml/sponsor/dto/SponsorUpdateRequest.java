package kpl.fiml.sponsor.dto;

import jakarta.validation.constraints.NotNull;
import kpl.fiml.project.domain.Reward;
import kpl.fiml.sponsor.domain.Sponsor;
import kpl.fiml.user.domain.User;
import lombok.Getter;

@Getter
public class SponsorUpdateRequest {

    @NotNull
    private Long rewardId;

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
