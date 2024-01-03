package kpl.fiml.user.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FollowingRepository extends JpaRepository<Following, Long> {
    Optional<Following> findByFollowingUserAndFollowerUser(User following, User follower);

    boolean existsByFollowingUserAndFollowerUser(User following, User follower);
}
