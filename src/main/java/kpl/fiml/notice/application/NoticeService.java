package kpl.fiml.notice.application;

import kpl.fiml.notice.domain.Notice;
import kpl.fiml.notice.domain.NoticeRepository;
import kpl.fiml.notice.dto.*;
import kpl.fiml.project.domain.Project;
import kpl.fiml.project.domain.ProjectRepository;
import kpl.fiml.user.domain.User;
import kpl.fiml.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    @Transactional
    public NoticeCreateResponse createNotice(Long userId, NoticeCreateRequest request) {
        User user = getUserByUserId(userId);
        Project project = getProjectByProjectId(request.getProjectId());

        if (!user.isSameUser(project.getUser())) {
            throw new IllegalArgumentException("프로젝트 생성자만 공지사항 작성이 가능합니다.");
        }
        Notice savedNotice = noticeRepository.save(request.toEntity(user, project));

        return NoticeCreateResponse.of(savedNotice.getId(), user.getId(), project.getId());
    }

    @Transactional
    public NoticeUpdateResponse updateNotice(Long userId, Long noticeId, NoticeUpdateRequest request) {
        Notice notice = getById(noticeId);
        User user = getUserByUserId(userId);

        if (!user.isSameUser(notice.getUser())) {
            throw new IllegalArgumentException("프로젝트 생성자만 공지사항 수정이 가능합니다.");
        }
        notice.updateContent(request.getContent());

        return NoticeUpdateResponse.of(noticeId, notice.getContent(), notice.getUser().getId());
    }

    public NoticeDto findById(Long noticeId) {
        Notice findNotice = getById(noticeId);

        return NoticeDto.of(findNotice.getId(), findNotice.getContent(), findNotice.getCreatedAt(), findNotice.getUpdatedAt());
    }

    public List<NoticeDto> findAllByUserId(Long userId) {
        List<Notice> findNoticeList = noticeRepository.findAllByUserIdAndDeletedAtIsNull(userId).get();

        return findNoticeList.stream()
                .map(notice -> NoticeDto.of(notice.getId(), notice.getContent(), notice.getCreatedAt(), notice.getUpdatedAt()))
                .toList();
    }

    public List<NoticeDto> findAllByProjectId(Long projectId) {
        List<Notice> findNoticeList = noticeRepository.findAllByProjectIdAndDeletedAtIsNull(projectId).get();

        return findNoticeList.stream()
                .map(notice -> NoticeDto.of(notice.getId(), notice.getContent(), notice.getCreatedAt(), notice.getUpdatedAt()))
                .toList();
    }

    @Transactional
    public NoticeDeleteResponse deleteById(Long noticeId, Long userId) {
        Notice deleteNotice = getById(noticeId);
        User user = getUserByUserId(userId);

        if (!user.isSameUser(deleteNotice.getUser())) {
            throw new IllegalArgumentException("프로젝트 생성자만 공지사항 삭제가 가능합니다.");
        }
        deleteNotice.deleteNotice();

        return NoticeDeleteResponse.of(deleteNotice.getId(), userId, deleteNotice.getDeletedAt());
    }

    private Notice getById(Long noticeId) {
        return noticeRepository.findByIdAndDeletedAtIsNull(noticeId)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 공지사항이 존재하지 않습니다."));
    }

    private User getUserByUserId(Long userId) {
        return userRepository.findByIdAndDeletedAtIsNull(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 사용자가 없습니다."));
    }

    private Project getProjectByProjectId(Long projectId) {
        return projectRepository.findById(projectId)
                .filter(project -> !project.isDeleted())
                .orElseThrow(() -> new IllegalArgumentException("해당하는 프로젝트가 존재하지 않습니다."));
    }
}
