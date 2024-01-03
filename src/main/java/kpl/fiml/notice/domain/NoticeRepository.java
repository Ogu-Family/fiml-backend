package kpl.fiml.notice.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long> {
    Optional<Notice> findByIdAndDeletedAtIsNull(Long id);
    Optional<List<Notice>> findAllByUserIdAndDeletedAtIsNull(Long userId);
    Optional<List<Notice>> findAllByProjectIdAndDeletedAtIsNull(Long projectId);
}
