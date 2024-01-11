package kpl.fiml.project.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import kpl.fiml.project.domain.Reward;
import lombok.*;

import java.util.List;

@Schema(description = "프로젝트 리워드 수정 정보")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProjectRewardUpdateRequest {

    @Schema(description = "프로젝트 리워드 리스트")
    private List<@Valid ProjectRewardRequest> rewards;

    public List<Reward> convertRewardEntities() {
        return rewards.stream()
                .map(projectRewardRequest ->
                        projectRewardRequest.toEntity(this.rewards.indexOf(projectRewardRequest)))
                .toList();
    }
}
