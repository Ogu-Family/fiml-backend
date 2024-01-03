package kpl.fiml.sponsor.domain;

import kpl.fiml.project.domain.Reward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SponsorRepository extends JpaRepository<Sponsor, Long> {
    Optional<Sponsor> findByIdAndDeletedAtIsNull(Long id);
    List<Sponsor> findAllByReward(Reward reward);
}
