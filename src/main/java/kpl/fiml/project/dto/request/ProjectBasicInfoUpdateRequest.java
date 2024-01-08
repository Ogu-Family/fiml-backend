package kpl.fiml.project.dto.request;

import kpl.fiml.project.domain.ProjectImage;
import kpl.fiml.project.domain.enums.ProjectCategory;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ProjectBasicInfoUpdateRequest {

    private String summary;
    private ProjectCategory category;
    private String title;
    private List<ProjectImageRequest> projectImages;

    public List<ProjectImage> getProjectImageEntities() {
        return this.projectImages.stream()
                .map(projectImageRequest ->
                        projectImageRequest.toEntity(this.projectImages.indexOf(projectImageRequest)))
                .toList();
    }
}
