package kpl.fiml.project.dto;

import jakarta.validation.constraints.NotNull;
import kpl.fiml.project.domain.ProjectImage;
import lombok.Getter;

@Getter
public class ProjectImageDto {

    @NotNull
    private String path;

    public ProjectImage toEntity(int listIndex) {
        return ProjectImage.builder()
                .sequence(listIndex)
                .path(path)
                .build();
    }
}
