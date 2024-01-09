package kpl.fiml.project.dto.request;

import jakarta.validation.constraints.NotNull;
import kpl.fiml.project.domain.ProjectImage;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProjectImageRequest {

    @NotNull
    private String path;

    public ProjectImage toEntity(int listIndex) {
        return ProjectImage.builder()
                .sequence(listIndex)
                .path(path)
                .build();
    }
}
