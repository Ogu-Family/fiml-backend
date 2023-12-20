package kpl.fiml.notice.application;

import kpl.fiml.notice.domain.Notice;
import kpl.fiml.notice.domain.NoticeRepository;
import kpl.fiml.notice.dto.NoticeCreateRequest;
import kpl.fiml.notice.dto.NoticeCreateResponse;
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
}
