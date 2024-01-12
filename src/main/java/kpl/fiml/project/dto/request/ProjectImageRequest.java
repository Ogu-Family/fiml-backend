package kpl.fiml.project.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import kpl.fiml.project.domain.ProjectImage;
import lombok.*;

@Schema(description = "프로젝트 이미지 정보")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProjectImageRequest {

    @Schema(description = "프로젝트 이미지 경로", example = "/project/image/1")
    @NotNull
    private String path;

    public ProjectImage toEntity(int listIndex) {
        return ProjectImage.builder()
                .sequence(listIndex)
                .path(path)
                .build();
    }
}
