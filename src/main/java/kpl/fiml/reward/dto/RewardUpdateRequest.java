package kpl.fiml.reward.dto;

import kpl.fiml.reward.domain.Reward;
import lombok.Getter;

import java.util.List;

@Getter
public class RewardUpdateRequest {

    private List<RewardDto> rewards;

    public List<Reward> toEntities() {
        return rewards.stream()
                .map(RewardDto::toEntity)
                .toList();
    }
}
