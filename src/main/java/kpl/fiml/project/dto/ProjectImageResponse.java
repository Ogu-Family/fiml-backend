package kpl.fiml.project.dto;

import kpl.fiml.project.domain.ProjectImage;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ProjectImageResponse {

    private final Integer sequence;
    private final String path;

    public static ProjectImageResponse of(ProjectImage projectImage) {
        return new ProjectImageResponse(
                projectImage.getSequence(),
                projectImage.getPath()
        );
    }
}
