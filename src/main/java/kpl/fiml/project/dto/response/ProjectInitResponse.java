package kpl.fiml.project.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ProjectInitResponse {

    private final Long id;

    public static ProjectInitResponse of(Long id) {
        return new ProjectInitResponse(id);
    }
}
