package kpl.fiml.project.domain;

import kpl.fiml.project.dto.request.ProjectListFindRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProjectRepositoryQuery {

    Page<Project> findWithSearchKeyword(ProjectListFindRequest searchRequest, Pageable pageable);
}
