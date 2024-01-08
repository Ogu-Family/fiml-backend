package kpl.fiml.project.dto.request;

import jakarta.validation.Valid;
import kpl.fiml.project.domain.Reward;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ProjectRewardUpdateRequest {

    private List<@Valid ProjectRewardRequest> rewards;

    public List<Reward> convertRewardEntities() {
        return rewards.stream()
                .map(projectRewardRequest ->
                        projectRewardRequest.toEntity(this.rewards.indexOf(projectRewardRequest)))
                .toList();
    }
}
