package kpl.fiml.sponsor.dto;

import kpl.fiml.project.domain.Reward;
import kpl.fiml.sponsor.domain.Sponsor;
import kpl.fiml.user.domain.User;
import lombok.Getter;

@Getter
public class SponsorUpdateRequest {

    private Long rewardId;
    private Long totalAmount;

    public Sponsor toEntity(User user, Reward reward) {
        return Sponsor.builder()
                .user(user)
                .reward(reward)
                .totalAmount(totalAmount)
                .build();
    }
}
