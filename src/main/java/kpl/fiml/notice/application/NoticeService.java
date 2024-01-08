package kpl.fiml.notice.application;

import kpl.fiml.notice.domain.Notice;
import kpl.fiml.notice.domain.NoticeRepository;
import kpl.fiml.notice.dto.NoticeDto;
import kpl.fiml.notice.dto.request.NoticeCreateRequest;
import kpl.fiml.notice.dto.request.NoticeUpdateRequest;
import kpl.fiml.notice.dto.response.NoticeCreateResponse;
import kpl.fiml.notice.dto.response.NoticeDeleteResponse;
import kpl.fiml.notice.dto.response.NoticeUpdateResponse;
import kpl.fiml.notice.exception.NoticeErrorCode;
import kpl.fiml.notice.exception.NoticeNotFoundException;
import kpl.fiml.notice.exception.NoticePermissionException;
import kpl.fiml.project.application.ProjectService;
import kpl.fiml.project.domain.Project;
import kpl.fiml.user.application.UserService;
import kpl.fiml.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NoticeService {

    private final UserService userService;
    private final ProjectService projectService;
    private final NoticeRepository noticeRepository;

    @Transactional
    public NoticeCreateResponse createNotice(Long userId, NoticeCreateRequest request) {
        User user = userService.getById(userId);
        Project project = projectService.getProjectByIdWithUser(request.getProjectId());

        validateNoticeCreationPermission(project.getUser(), user);
        Notice savedNotice = noticeRepository.save(request.toEntity(user, project));

        return NoticeCreateResponse.of(savedNotice.getId(), user.getId(), project.getId());
    }

    @Transactional
    public NoticeUpdateResponse updateNotice(Long userId, Long noticeId, NoticeUpdateRequest request) {
        Notice notice = getById(noticeId);
        User user = userService.getById(userId);

        notice.updateContent(Objects.requireNonNull(request.getContent(), "content가 null 입니다."), user);

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
        User user = userService.getById(userId);

        deleteNotice.deleteNotice(user);

        return NoticeDeleteResponse.of(deleteNotice.getId(), userId, deleteNotice.getDeletedAt());
    }

    public Notice getById(Long noticeId) {
        return noticeRepository.findByIdAndDeletedAtIsNull(noticeId)
                .orElseThrow(NoticeNotFoundException::new);
    }

    private void validateNoticeCreationPermission(User projectUser, User loginUser) {
        if (!loginUser.isSameUser(projectUser)) {
            throw new NoticePermissionException(NoticeErrorCode.ACCESS_DENIED);
        }
    }
}
