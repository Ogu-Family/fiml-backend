package kpl.fiml.project.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RewardRepository extends JpaRepository<Reward, Long> {
    Optional<Reward> findByIdAndDeletedAtIsNull(Long id);
    List<Reward> findAllByProject(Project project);
}
