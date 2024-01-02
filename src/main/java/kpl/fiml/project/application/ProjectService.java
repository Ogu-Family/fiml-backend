package kpl.fiml.project.application;

import kpl.fiml.global.dto.PageResponse;
import kpl.fiml.project.domain.Project;
import kpl.fiml.project.domain.ProjectRepository;
import kpl.fiml.project.dto.*;
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

    @Transactional
    public ProjectInitResponse initProject(ProjectInitRequest request, User user) {
        return ProjectInitResponse.of(projectRepository.save(request.toEntity(user)).getId());
    }

    @Transactional
    public void updateBasicInfo(Long projectId, ProjectBasicInfoUpdateRequest request, User user) {
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
    public void updateIntroduction(Long projectId, ProjectDetailIntroductionUpdateRequest request, User user) {
        this.getProjectById(projectId)
                .updateIntroduction(request.getIntroduction(), user);
    }

    @Transactional
    public void updateFundingPlan(Long projectId, ProjectFundingPlanUpdateRequest request, User user) {
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
    public void updateRewards(Long projectId, ProjectRewardUpdateRequest request, User user) {
        this.getProjectById(projectId)
                .updateRewards(request.getRewardEntities(), user);
    }

    @Transactional
    public void submitProject(Long projectId, User user) {
        this.getProjectById(projectId)
                .submit(user);
    }

    public PageResponse<Project, ProjectDto> findProjectsBySearchConditions(ProjectListFindRequest request) {
        return PageResponse.of(
                projectRepository.findWithSearchKeyword(request, PageRequest.of(request.getPage(), request.getSize())), ProjectDto::of
        );
    }

    private Project getProjectById(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 프로젝트입니다."));
    }
}
