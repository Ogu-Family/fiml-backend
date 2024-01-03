package kpl.fiml.project.application;

import kpl.fiml.global.dto.PageResponse;
import kpl.fiml.project.domain.Project;
import kpl.fiml.project.domain.ProjectLike;
import kpl.fiml.project.domain.ProjectLikeRepository;
import kpl.fiml.project.domain.ProjectRepository;
import kpl.fiml.project.dto.*;
import kpl.fiml.user.application.UserService;
import kpl.fiml.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        this.getProjectById(projectId)
                .updateBasicInfo(
                        request.getSummary(),
                        request.getCategory(),
                        request.getTitle(),
                        request.getProjectImageEntities(),
                        user
                );
    }

    @Transactional
    public void updateIntroduction(Long projectId, ProjectDetailIntroductionUpdateRequest request, Long userId) {
        User user = userService.getById(userId);

        this.getProjectById(projectId)
                .updateIntroduction(request.getIntroduction(), user);
    }

    @Transactional
    public void updateFundingPlan(Long projectId, ProjectFundingPlanUpdateRequest request, Long userId) {
        User user = userService.getById(userId);

        this.getProjectById(projectId)
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

        this.getProjectById(projectId)
                .updateRewards(request.getRewardEntities(), user);
    }

    @Transactional
    public void submitProject(Long projectId, Long userId) {
        User user = userService.getById(userId);

        this.getProjectById(projectId)
                .submit(user);
    }

    public PageResponse<Project, ProjectDto> findProjectsBySearchConditions(ProjectListFindRequest request) {
        return PageResponse.of(
                projectRepository.findWithSearchKeyword(request, PageRequest.of(request.getPage(), request.getSize())), ProjectDto::of
        );
    }

    @Transactional
    public void likeProject(Long projectId, Long userId) {
        Project project = getProjectById(projectId);
        User user = userService.getById(userId);

        checkIfProjectAlreadyLiked(project, user);

        projectLikeRepository.save(
                ProjectLike.builder()
                        .project(project)
                        .user(user)
                        .build()
        );
    }

    private void checkIfProjectAlreadyLiked(Project project, User user) {
        if (projectLikeRepository.existsByProjectAndUser(project, user)) {
            throw new IllegalArgumentException("이미 좋아요를 누른 프로젝트입니다.");
        }
    }

    @Transactional
    public void unlikeProject(Long projectId, Long id) {
        Project project = getProjectById(projectId);
        User user = userService.getById(id);

        ProjectLike projectLike = getByProjectAndUser(project, user);

        projectLikeRepository.delete(projectLike);
    }

    private ProjectLike getByProjectAndUser(Project project, User user) {
        return projectLikeRepository.findByProjectAndUser(project, user)
                .orElseThrow(() -> new IllegalArgumentException("좋아요를 누르지 않은 프로젝트입니다."));
    }

    private Project getProjectById(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 프로젝트입니다."));
    }
}
