package kpl.fiml.sponsor.dto;

import jakarta.validation.constraints.NotBlank;
import kpl.fiml.project.domain.Reward;
import kpl.fiml.sponsor.domain.Sponsor;
import kpl.fiml.user.domain.User;
import lombok.Getter;

@Getter
public class SponsorCreateRequest {

    @NotBlank
    private Long rewardId;

    @NotBlank
    private Long totalAmount;

    public Sponsor toEntity(User user, Reward reward) {
        return Sponsor.builder()
                .user(user)
                .reward(reward)
                .totalAmount(totalAmount)
                .build();
    }
}
