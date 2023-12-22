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

        return new NoticeCreateResponse(savedNotice.getId());
    }

    @Transactional
    public NoticeUpdateResponse updateNotice(Long noticeId, NoticeUpdateRequest request) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 공지사항이 존재하지 않습니다."));
        notice.update(request.getContent());
        return new NoticeUpdateResponse(noticeId, notice.getContent());
    }

    @Transactional(readOnly = true)
    public NoticeDto findById(Long noticeId) {
        Notice findNotice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 공지사항이 존재하지 않습니다."));
        return new NoticeDto(findNotice.getId(), findNotice.getContent(), findNotice.getCreatedAt(), findNotice.getUpdatedAt());

    }

    @Transactional
    public Long deleteById(Long noticeId) {
        noticeRepository.deleteById(noticeId);
        return noticeId;
    }
}
