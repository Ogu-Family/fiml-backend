package kpl.fiml.notice.application;

import kpl.fiml.notice.domain.Notice;
import kpl.fiml.notice.domain.NoticeRepository;
import kpl.fiml.notice.dto.NoticeCreateRequest;
import kpl.fiml.notice.dto.NoticeCreateResponse;
import kpl.fiml.notice.dto.NoticeUpdateRequest;
import kpl.fiml.notice.dto.NoticeUpdateResponse;
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
                .orElseThrow(() -> new IllegalArgumentException("해당하는 공지사항이 존재하지 않습니다"));
        notice.update(request.getContent());
        return new NoticeUpdateResponse(noticeId, notice.getContent());
    }
}
