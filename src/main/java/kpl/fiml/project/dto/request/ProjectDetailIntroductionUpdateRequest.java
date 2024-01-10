package kpl.fiml.project.dto.request;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProjectDetailIntroductionUpdateRequest {

    private String introduction;
}
