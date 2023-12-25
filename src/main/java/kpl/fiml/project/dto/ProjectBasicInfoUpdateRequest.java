package kpl.fiml.project.dto;

import kpl.fiml.project.domain.enums.ProjectCategory;
import lombok.Getter;

import java.util.List;

@Getter
public class ProjectBasicInfoUpdateRequest {

    private String summary;
    private ProjectCategory category;
    private String title;
    private List<ProjectImageDto> projectImages;
}
