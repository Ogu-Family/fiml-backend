package kpl.fiml.project.application;

import kpl.fiml.project.domain.ProjectRepository;
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
        return new ProjectInitResponse(projectRepository.save(request.toEntity()).getId());
    }
}
