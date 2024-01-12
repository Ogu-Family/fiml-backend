package kpl.fiml.project.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "프로젝트 소개 수정 정보")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProjectDetailIntroductionUpdateRequest {

    @Schema(description = "프로젝트 소개", example = "프로젝트 소개")
    private String introduction;
}
