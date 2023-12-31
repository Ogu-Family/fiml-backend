package kpl.fiml.project.domain;

import kpl.fiml.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjectLikeRepository extends JpaRepository<ProjectLike, Long> {
    Optional<ProjectLike> findByProjectAndUser(Project project, User user);

    boolean existsByProjectAndUser(Project project, User user);
}
