package kpl.fiml.project.presentation;

import kpl.fiml.global.dto.PageResponse;
import kpl.fiml.project.application.ProjectService;
import kpl.fiml.project.domain.Project;
import kpl.fiml.project.dto.*;
import kpl.fiml.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping("/projects")
    public ResponseEntity<ProjectInitResponse> initProject(@RequestBody ProjectInitRequest request, @AuthenticationPrincipal User user) {
        ProjectInitResponse response = this.projectService.initProject(request, user);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/projects/{projectId}/basic-info")
    public ResponseEntity<Void> updateBasicInfo(@PathVariable("projectId") Long projectId,
                                                @RequestBody ProjectBasicInfoUpdateRequest request) {
        this.projectService.updateBasicInfo(projectId, request);

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/projects/{projectId}/introduction")
    public ResponseEntity<Void> updateIntroduction(@PathVariable("projectId") Long projectId,
                                                   @RequestBody ProjectDetailIntroductionUpdateRequest request) {
        this.projectService.updateIntroduction(projectId, request);

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/projects/{projectId}/funding-plan")
    public ResponseEntity<Void> updateFundingPlan(@PathVariable("projectId") Long projectId,
                                                  @RequestBody ProjectFundingPlanUpdateRequest request) {
        this.projectService.updateFundingPlan(projectId, request);

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/projects/{projectId}/rewards")
    public ResponseEntity<Void> updateRewards(@PathVariable("projectId") Long projectId,
                                              @RequestBody ProjectRewardUpdateRequest request) {
        this.projectService.updateRewards(projectId, request);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/projects/{projectId}/submit")
    public ResponseEntity<Void> submitProject(@PathVariable("projectId") Long projectId) {
        this.projectService.submitProject(projectId);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/projects")
    public ResponseEntity<PageResponse<Project, ProjectDto>> findProjects(@ModelAttribute ProjectListFindRequest request) {
        PageResponse<Project, ProjectDto> projectsBySearchConditions = this.projectService.findProjectsBySearchConditions(request);

        return ResponseEntity.ok(projectsBySearchConditions);
    }
}
