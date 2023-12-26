package kpl.fiml.project.dto;

import kpl.fiml.project.domain.ProjectImage;
import lombok.Getter;

@Getter
public class ProjectImageDto {

    private Integer sequence;
    private String path;

    public ProjectImage toEntity() {
        return ProjectImage.builder()
                .sequence(sequence)
                .path(path)
                .build();
    }
}
