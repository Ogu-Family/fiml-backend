package kpl.fiml.sponsor.domain;

import kpl.fiml.project.domain.Reward;
import kpl.fiml.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SponsorRepository extends JpaRepository<Sponsor, Long> {
    Optional<Sponsor> findByIdAndUser(Long id, User user);
    List<Sponsor> findAllByReward(Reward reward);
}
