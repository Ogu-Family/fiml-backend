package kpl.fiml.project.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProjectDetailIntroductionUpdateRequest {

    private String introduction;
}
