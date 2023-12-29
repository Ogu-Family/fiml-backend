package kpl.fiml.notice.application;

import kpl.fiml.notice.domain.Notice;
import kpl.fiml.notice.domain.NoticeRepository;
import kpl.fiml.notice.dto.*;
import kpl.fiml.user.domain.User;
import kpl.fiml.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final UserRepository userRepository;

    @Transactional
    public NoticeCreateResponse createNotice(NoticeCreateRequest request) {
        User user = getUserByUserId(request.getUserId());
        Notice savedNotice = noticeRepository.save(request.toEntity(user));

        return NoticeCreateResponse.of(savedNotice.getId(), user.getId());
    }

    @Transactional
    public NoticeUpdateResponse updateNotice(Long noticeId, NoticeUpdateRequest request) {
        Notice notice = getById(noticeId);
        notice.updateContent(request.getContent());

        return NoticeUpdateResponse.of(noticeId, notice.getContent());
    }

    @Transactional(readOnly = true)
    public NoticeDto findById(Long noticeId) {
        Notice findNotice = getById(noticeId);

        return NoticeDto.of(findNotice.getId(), findNotice.getContent(), findNotice.getCreatedAt(), findNotice.getUpdatedAt());
    }

    @Transactional
    public NoticeDeleteResponse deleteById(Long noticeId) {
        Notice deleteNotice = getById(noticeId);
        deleteNotice.delete();

        return NoticeDeleteResponse.of(deleteNotice.getId(), deleteNotice.getDeletedAt());
    }

    private Notice getById(Long noticeId) {
        return noticeRepository.findById(noticeId)
                .filter(notice -> !notice.isDeleted())
                .orElseThrow(() -> new IllegalArgumentException("해당하는 공지사항이 존재하지 않습니다."));
    }

    private User getUserByUserId(Long userId) {
        return userRepository.findById(userId)
                .filter(user -> !user.isDeleted())
                .orElseThrow(() -> new IllegalArgumentException("해당하는 사용자가 없습니다."));
    }
}
