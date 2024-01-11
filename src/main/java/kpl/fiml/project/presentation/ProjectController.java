package kpl.fiml.project.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kpl.fiml.global.dto.PageResponse;
import kpl.fiml.project.application.ProjectService;
import kpl.fiml.project.domain.Project;
import kpl.fiml.project.dto.request.*;
import kpl.fiml.project.dto.response.ProjectDetailResponse;
import kpl.fiml.project.dto.response.ProjectInitResponse;
import kpl.fiml.project.dto.response.ProjectResponse;
import kpl.fiml.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Project Controller", description = "프로젝트 컨트롤러")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ProjectController {

    private final ProjectService projectService;

    @Operation(summary = "프로젝트 생성", description = "프로젝트 세부 정보 입력 전 초기화")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "프로젝트 초기화 생성 성공")
    })
    @PostMapping("/projects")
    public ResponseEntity<ProjectInitResponse> initProject(@RequestBody @Valid ProjectInitRequest request, @AuthenticationPrincipal User user) {
        ProjectInitResponse response = this.projectService.initProject(request, user.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "프로젝트 기본 정보 수정", description = "프로젝트 기본 정보 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "프로젝트 기본 정보 수정 성공"),
            @ApiResponse(responseCode = "403", description = "프로젝트 수정 권한 없음"),
            @ApiResponse(responseCode = "404", description = "프로젝트를 찾을 수 없음")
    })
    @Parameters({
            @Parameter(name = "projectId", description = "프로젝트 ID", required = true),
    })
    @PatchMapping("/projects/{projectId}/basic-info")
    public ResponseEntity<Void> updateBasicInfo(@PathVariable("projectId") Long projectId,
                                                @RequestBody @Valid ProjectBasicInfoUpdateRequest request,
                                                @AuthenticationPrincipal User user) {
        this.projectService.updateBasicInfo(projectId, request, user.getId());

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "프로젝트 소개 수정", description = "프로젝트 소개 설명 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "프로젝트 소개 수정 성공"),
            @ApiResponse(responseCode = "403", description = "프로젝트 수정 권한 없음"),
            @ApiResponse(responseCode = "404", description = "프로젝트를 찾을 수 없음")
    })
    @Parameters({
            @Parameter(name = "projectId", description = "프로젝트 ID", required = true),
    })
    @PatchMapping("/projects/{projectId}/introduction")
    public ResponseEntity<Void> updateIntroduction(@PathVariable("projectId") Long projectId,
                                                   @RequestBody ProjectDetailIntroductionUpdateRequest request,
                                                   @AuthenticationPrincipal User user) {
        this.projectService.updateIntroduction(projectId, request, user.getId());

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "프로젝트 펀딩 정보 수정", description = "프로젝트 펀딩 정보 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "프로젝트 펀딩 정보 수정 성공"),
            @ApiResponse(responseCode = "403", description = "프로젝트 수정 권한 없음"),
            @ApiResponse(responseCode = "404", description = "프로젝트를 찾을 수 없음")
    })
    @Parameters({
            @Parameter(name = "projectId", description = "프로젝트 ID", required = true),
    })
    @PatchMapping("/projects/{projectId}/funding-plan")
    public ResponseEntity<Void> updateFundingPlan(@PathVariable("projectId") Long projectId,
                                                  @RequestBody ProjectFundingPlanUpdateRequest request,
                                                  @AuthenticationPrincipal User user) {
        this.projectService.updateFundingPlan(projectId, request, user.getId());

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "프로젝트 리워드 수정", description = "프로젝트 리워드 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "프로젝트 리워드 수정 성공"),
            @ApiResponse(responseCode = "403", description = "프로젝트 수정 권한 없음"),
            @ApiResponse(responseCode = "404", description = "프로젝트를 찾을 수 없음")
    })
    @Parameters({
            @Parameter(name = "projectId", description = "프로젝트 ID", required = true),
    })
    @PatchMapping("/projects/{projectId}/rewards")
    public ResponseEntity<Void> updateRewards(@PathVariable("projectId") Long projectId,
                                              @RequestBody @Valid ProjectRewardUpdateRequest request,
                                              @AuthenticationPrincipal User user) {
        this.projectService.updateRewards(projectId, request, user.getId());

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "프로젝트 최종 업로드", description = "프로젝트 정보 모두 입력 후 최종 업로드")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "프로젝트 최종 업로드 성공"),
            @ApiResponse(responseCode = "400 - 1", description = "프로젝트 정보 모두 입력하지 않음"),
            @ApiResponse(responseCode = "400 - 2", description = "프로젝트 업로드 가능 상태가 아님"),
            @ApiResponse(responseCode = "400 - 3", description = "프로젝트 펀딩 기간이 올바르지 않음"),
            @ApiResponse(responseCode = "404", description = "프로젝트를 찾을 수 없음")
    })
    @Parameters({
            @Parameter(name = "projectId", description = "프로젝트 ID", required = true),
    })
    @PostMapping("/projects/{projectId}/submit")
    public ResponseEntity<Void> submitProject(@PathVariable("projectId") Long projectId, @AuthenticationPrincipal User user) {
        this.projectService.submitProject(projectId, user.getId());

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "프로젝트 좋아요", description = "프로젝트 좋아요")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "프로젝트 좋아요 성공"),
            @ApiResponse(responseCode = "400", description = "이미 좋아요한 프로젝트"),
            @ApiResponse(responseCode = "404", description = "프로젝트를 찾을 수 없음")
    })
    @Parameters({
            @Parameter(name = "projectId", description = "프로젝트 ID", required = true),
    })
    @PostMapping("/projects/{projectId}/like")
    public ResponseEntity<Void> likeProject(@PathVariable("projectId") Long projectId, @AuthenticationPrincipal User user) {
        this.projectService.likeProject(projectId, user.getId());

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "프로젝트 좋아요 취소", description = "프로젝트 좋아요 취소")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "프로젝트 좋아요 취소 성공"),
            @ApiResponse(responseCode = "400", description = "좋아요하지 않은 프로젝트"),
            @ApiResponse(responseCode = "404", description = "프로젝트를 찾을 수 없음")
    })
    @Parameters({
            @Parameter(name = "projectId", description = "프로젝트 ID", required = true),
    })
    @PostMapping("/projects/{projectId}/unlike")
    public ResponseEntity<Void> unlikeProject(@PathVariable("projectId") Long projectId, @AuthenticationPrincipal User user) {
        this.projectService.unlikeProject(projectId, user.getId());

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "프로젝트 목록 조회", description = "프로젝트 목록 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "프로젝트 목록 조회 성공")
    })
    @GetMapping("/projects")
    public ResponseEntity<PageResponse<Project, ProjectResponse>> findProjects(@ModelAttribute ProjectListFindRequest request) {
        PageResponse<Project, ProjectResponse> projectsBySearchConditions = this.projectService.findProjectsBySearchConditions(request);

        return ResponseEntity.ok(projectsBySearchConditions);
    }

    @Operation(summary = "프로젝트 상세 조회", description = "프로젝트 상세 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "프로젝트 상세 조회 성공"),
            @ApiResponse(responseCode = "404", description = "프로젝트를 찾을 수 없음")
    })
    @Parameters({
            @Parameter(name = "projectId", description = "프로젝트 ID", required = true),
    })
    @GetMapping("/projects/{projectId}")
    public ResponseEntity<ProjectDetailResponse> findProjectDetail(@PathVariable("projectId") Long projectId) {
        ProjectDetailResponse projectDetail = this.projectService.findProjectDetail(projectId);

        return ResponseEntity.ok(projectDetail);
    }

    @Operation(summary = "프로젝트 삭제", description = "프로젝트 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "프로젝트 삭제 성공"),
            @ApiResponse(responseCode = "403", description = "프로젝트 삭제 권한 없음"),
            @ApiResponse(responseCode = "404", description = "프로젝트를 찾을 수 없음")
    })
    @Parameters({
            @Parameter(name = "projectId", description = "프로젝트 ID", required = true),
    })
    @DeleteMapping("/projects/{projectId}")
    public ResponseEntity<Void> deleteProject(@PathVariable("projectId") Long projectId, @AuthenticationPrincipal User user) {
        this.projectService.deleteProject(projectId, user.getId());

        return ResponseEntity.ok().build();
    }
}
