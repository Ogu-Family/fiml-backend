package kpl.fiml.project.dto.request;

import jakarta.validation.Valid;
import kpl.fiml.project.domain.Reward;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectRewardUpdateRequest {

    private List<@Valid ProjectRewardRequest> rewards;

    public List<Reward> convertRewardEntities() {
        return rewards.stream()
                .map(projectRewardRequest ->
                        projectRewardRequest.toEntity(this.rewards.indexOf(projectRewardRequest)))
                .toList();
    }
}
