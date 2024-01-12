package kpl.fiml.project.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import kpl.fiml.project.domain.ProjectImage;
import kpl.fiml.project.domain.enums.ProjectCategory;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Schema(description = "프로젝트 기본 정보 수정 정보")
@Getter
@Builder
public class ProjectBasicInfoUpdateRequest {

    @Schema(description = "프로젝트 요약", example = "프로젝트 요약")
    private String summary;

    @Schema(description = "프로젝트 카테고리", example = "DIGITAL_GAME")
    private ProjectCategory category;

    @Schema(description = "프로젝트 제목", example = "프로젝트 제목")
    private String title;

    @Schema(description = "프로젝트 이미지 리스트")
    private List<@Valid ProjectImageRequest> projectImages;

    public List<ProjectImage> convertProjectImageEntities() {
        return this.projectImages.stream()
                .map(projectImageRequest ->
                        projectImageRequest.toEntity(this.projectImages.indexOf(projectImageRequest)))
                .toList();
    }
}
