package kpl.fiml.notice.application;

import kpl.fiml.notice.domain.Notice;
import kpl.fiml.notice.domain.NoticeRepository;
import kpl.fiml.notice.dto.*;
import kpl.fiml.user.domain.User;
import kpl.fiml.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final UserRepository userRepository;

    @Transactional
    public NoticeCreateResponse createNotice(Long userId, NoticeCreateRequest request) {
        User user = getUserByUserId(userId);
        Notice savedNotice = noticeRepository.save(request.toEntity(user));

        return NoticeCreateResponse.of(savedNotice.getId(), user.getId());
    }

    @Transactional
    public NoticeUpdateResponse updateNotice(Long userId, Long noticeId, NoticeUpdateRequest request) {
        Notice notice = getById(noticeId);
        validateUser(userId, notice.getUser());
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

    @Transactional
    public NoticeDeleteResponse deleteById(Long noticeId, Long userId) {
        Notice deleteNotice = getById(noticeId);
        validateUser(userId, deleteNotice.getUser());
        deleteNotice.delete();

        return NoticeDeleteResponse.of(deleteNotice.getId(), userId, deleteNotice.getDeletedAt());
    }

    private void validateUser(Long userId, User user) {
        if(!userId.equals(user.getId())) {
            // TODO : 프로젝트 생성자만 작성 가능한 로직으로 변경
            throw new IllegalArgumentException("공지사항 작성자만 접근 가능합니다.");
        }
    }

    private Notice getById(Long noticeId) {
        return noticeRepository.findByIdAndDeletedAtIsNull(noticeId)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 공지사항이 존재하지 않습니다."));
    }

    private User getUserByUserId(Long userId) {
        return userRepository.findByIdAndDeletedAtIsNull(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 사용자가 없습니다."));
    }
}
