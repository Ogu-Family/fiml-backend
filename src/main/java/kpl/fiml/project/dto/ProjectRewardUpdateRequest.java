package kpl.fiml.project.dto;

import kpl.fiml.project.domain.Reward;
import lombok.Getter;

import java.util.List;

@Getter
public class ProjectRewardUpdateRequest {

    private List<ProjectRewardDto> rewards;

    public List<Reward> getRewardEntities() {
        return rewards.stream()
                .map(projectRewardDto ->
                        projectRewardDto.toEntity(this.rewards.indexOf(projectRewardDto)))
                .toList();
    }
}
