package kpl.fiml.project.application;

import kpl.fiml.global.dto.PageResponse;
import kpl.fiml.project.domain.Project;
import kpl.fiml.project.domain.ProjectLike;
import kpl.fiml.project.domain.ProjectLikeRepository;
import kpl.fiml.project.domain.ProjectRepository;
import kpl.fiml.project.dto.request.*;
import kpl.fiml.project.dto.response.ProjectDetailResponse;
import kpl.fiml.project.dto.response.ProjectInitResponse;
import kpl.fiml.project.dto.response.ProjectResponse;
import kpl.fiml.project.exception.project.ProjectErrorCode;
import kpl.fiml.project.exception.project.ProjectLikeException;
import kpl.fiml.project.exception.project.ProjectFoundException;
import kpl.fiml.user.application.UserService;
import kpl.fiml.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectLikeRepository projectLikeRepository;
    private final UserService userService;

    @Transactional
    public ProjectInitResponse initProject(ProjectInitRequest request, Long userId) {
        User user = userService.getById(userId);

        return ProjectInitResponse.of(projectRepository.save(request.toEntity(user)).getId());
    }

    @Transactional
    public void updateBasicInfo(Long projectId, ProjectBasicInfoUpdateRequest request, Long userId) {
        User user = userService.getById(userId);

        this.getProjectByIdWithUser(projectId)
                .updateBasicInfo(
                        request.getSummary(),
                        request.getCategory(),
                        request.getTitle(),
                        request.convertProjectImageEntities(),
                        user
                );
    }

    @Transactional
    public void updateIntroduction(Long projectId, ProjectDetailIntroductionUpdateRequest request, Long userId) {
        User user = userService.getById(userId);

        this.getProjectByIdWithUser(projectId)
                .updateIntroduction(request.getIntroduction(), user);
    }

    @Transactional
    public void updateFundingPlan(Long projectId, ProjectFundingPlanUpdateRequest request, Long userId) {
        User user = userService.getById(userId);

        this.getProjectByIdWithUser(projectId)
                .updateFundingInfo(
                        request.getGoalAmount(),
                        request.getFundingStartDateTime(),
                        request.getFundingEndDate(),
                        request.getCommissionRate(),
                        user
                );
    }

    @Transactional
    public void updateRewards(Long projectId, ProjectRewardUpdateRequest request, Long userId) {
        User user = userService.getById(userId);

        this.getProjectByIdWithUser(projectId)
                .updateRewards(request.convertRewardEntities(), user);
    }

    @Transactional
    public void submitProject(Long projectId, Long userId) {
        User user = userService.getById(userId);

        this.getProjectByIdWithUser(projectId)
                .submit(user);
    }

    public PageResponse<Project, ProjectResponse> findProjectsBySearchConditions(ProjectListFindRequest request) {
        return PageResponse.of(
                projectRepository.findWithSearchKeyword(
                        request,
                        PageRequest.of(Optional.ofNullable(request.getPage()).orElse(0), Optional.ofNullable(request.getSize()).orElse(10))
                ),
                ProjectResponse::of
        );
    }

    public ProjectDetailResponse findProjectDetail(Long projectId) {
        return ProjectDetailResponse.of(this.getProjectByIdAndNotWritingStatusWithUser(projectId));
    }

    @Transactional
    public void likeProject(Long projectId, Long userId) {
        Project project = getProjectByIdWithUser(projectId);
        User user = userService.getById(userId);

        checkIfProjectAlreadyLiked(project, user);

        projectRepository.increaseLikeCount(projectId);
        projectLikeRepository.save(
                ProjectLike.builder()
                        .project(project)
                        .user(user)
                        .build()
        );
    }

    private void checkIfProjectAlreadyLiked(Project project, User user) {
        if (projectLikeRepository.existsByProjectAndUser(project, user)) {
            throw new ProjectLikeException(ProjectErrorCode.ALREADY_LIKED_PROJECT);
        }
    }

    @Transactional
    public void unlikeProject(Long projectId, Long id) {
        Project project = getProjectByIdWithUser(projectId);
        User user = userService.getById(id);

        ProjectLike projectLike = getProjectLikeByProjectAndUser(project, user);

        projectRepository.decreaseLikeCount(projectId);
        projectLikeRepository.delete(projectLike);
    }

    private ProjectLike getProjectLikeByProjectAndUser(Project project, User user) {
        return projectLikeRepository.findByProjectAndUser(project, user)
                .orElseThrow(() -> new ProjectLikeException(ProjectErrorCode.NOT_LIKED_PROJECT));
    }

    public Project getProjectByIdWithUser(Long projectId) {
        return projectRepository.findByIdWithUser(projectId)
                .orElseThrow(() -> new ProjectFoundException(ProjectErrorCode.PROJECT_NOT_FOUND));
    }

    private Project getProjectByIdAndNotWritingStatusWithUser(Long projectId) {
        return projectRepository.findByIdAndIsNotWritingStatusWithUser(projectId)
                .orElseThrow(() -> new ProjectFoundException(ProjectErrorCode.PROJECT_NOT_FOUND));
    }

    @Transactional
    public void deleteProject(Long projectId, Long id) {
        Project project = getProjectByIdWithUser(projectId);
        User user = userService.getById(id);

        project.deleteProject(user);
    }
}
