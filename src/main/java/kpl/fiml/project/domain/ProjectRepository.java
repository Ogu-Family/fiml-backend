package kpl.fiml.project.domain;

import kpl.fiml.project.domain.enums.ProjectStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long>, ProjectRepositoryQuery {

    Optional<Project> findByIdAndDeletedAtIsNull(Long id);
    List<Project> findAllByStatus(ProjectStatus status);
    @Query("select p from Project p join fetch p.user where p.id = :projectId and p.status != 'WRITING' and p.deletedAt is null")
    Optional<Project> findByIdAndIsNotWritingStatusWithUser(Long projectId);
}
