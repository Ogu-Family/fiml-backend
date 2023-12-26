package kpl.fiml.project.presentation;

import kpl.fiml.project.application.ProjectService;
import kpl.fiml.project.dto.ProjectBasicInfoUpdateRequest;
import kpl.fiml.project.dto.ProjectInitRequest;
import kpl.fiml.project.dto.ProjectInitResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping("/projects")
    public ResponseEntity<ProjectInitResponse> initProject(@RequestBody ProjectInitRequest request) {
        ProjectInitResponse response = this.projectService.initProject(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/projects/{projectId}/basic-info")
    public ResponseEntity<Void> updateBasicInfo(@PathVariable("projectId") Long projectId,
                                                @RequestBody ProjectBasicInfoUpdateRequest request) {
        this.projectService.updateBasicInfo(projectId, request);

        return ResponseEntity.ok().build();
    }
}
