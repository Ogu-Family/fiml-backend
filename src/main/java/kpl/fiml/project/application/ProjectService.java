package kpl.fiml.project.application;

import kpl.fiml.project.domain.Project;
import kpl.fiml.project.domain.ProjectRepository;
import kpl.fiml.project.dto.ProjectBasicInfoUpdateRequest;
import kpl.fiml.project.dto.ProjectInitRequest;
import kpl.fiml.project.dto.ProjectInitResponse;
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

    private Project getProjectById(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 프로젝트입니다."));
    }
}
