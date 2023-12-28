package kpl.fiml.project.dto;

import kpl.fiml.project.domain.ProjectImage;
import kpl.fiml.project.domain.enums.ProjectCategory;
import lombok.Getter;

import java.util.List;

@Getter
public class ProjectBasicInfoUpdateRequest {

    private String summary;
    private ProjectCategory category;
    private String title;
    private List<ProjectImageDto> projectImages;

    public List<ProjectImage> getProjectImageEntities() {
        return this.projectImages.stream()
                .map(projectImageDto ->
                        projectImageDto.toEntity(this.projectImages.indexOf(projectImageDto)))
                .toList();
    }
}
