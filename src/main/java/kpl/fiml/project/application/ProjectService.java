package kpl.fiml.project.application;

import kpl.fiml.project.domain.Project;
import kpl.fiml.project.domain.ProjectRepository;
import kpl.fiml.project.dto.*;
import kpl.fiml.project.dto.ProjectRewardUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;

    @Transactional
    public ProjectInitResponse initProject(ProjectInitRequest request) {
        return ProjectInitResponse.of(projectRepository.save(request.toEntity()).getId());
    }

    @Transactional
    public void updateBasicInfo(Long projectId, ProjectBasicInfoUpdateRequest request) {
        this.getProjectById(projectId)
                .updateBasicInfo(
                        request.getSummary(),
                        request.getCategory(),
                        request.getTitle(),
                        request.getProjectImageEntities()
                );
    }

    @Transactional
    public void updateIntroduction(Long projectId, ProjectDetailIntroductionUpdateRequest request) {
        this.getProjectById(projectId)
                .updateIntroduction(request.getIntroduction());
    }

    @Transactional
    public void updateFundingPlan(Long projectId, ProjectFundingPlanUpdateRequest request) {
        this.getProjectById(projectId)
                .updateFundingInfo(
                        request.getGoalAmount(),
                        request.getFundingStartDateTime(),
                        request.getFundingEndDate(),
                        request.getCommissionRate()
                );
    }

    @Transactional
    public void updateRewards(Long projectId, ProjectRewardUpdateRequest request) {
        this.getProjectById(projectId)
                .updateRewards(request.getRewardEntities());
    }

    private Project getProjectById(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 프로젝트입니다."));
    }
}
