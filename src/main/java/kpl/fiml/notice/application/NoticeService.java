package kpl.fiml.notice.application;

import kpl.fiml.notice.domain.Notice;
import kpl.fiml.notice.domain.NoticeRepository;
import kpl.fiml.notice.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;

    @Transactional
    public NoticeCreateResponse createNotice(NoticeCreateRequest request) {
        Notice savedNotice = noticeRepository.save(request.toEntity());

        return NoticeCreateResponse.of(savedNotice.getId());
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
}
