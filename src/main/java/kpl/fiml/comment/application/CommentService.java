package kpl.fiml.comment.application;

import kpl.fiml.comment.domain.Comment;
import kpl.fiml.comment.domain.CommentRepository;
import kpl.fiml.comment.dto.CommentCreateRequest;
import kpl.fiml.comment.dto.CommentCreateResponse;
import kpl.fiml.notice.domain.Notice;
import kpl.fiml.notice.domain.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {

    private final NoticeRepository noticeRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public CommentCreateResponse create(Long noticeId, CommentCreateRequest request) {
        Notice notice = getNoticeById(noticeId);
        Comment createdComment = commentRepository.save(request.toEntity(notice));

        return CommentCreateResponse.of(createdComment.getId(), createdComment.getContent(), createdComment.getCommenter(), noticeId);
    }

    private Notice getNoticeById(Long noticeId) {
        return noticeRepository.findById(noticeId)
                .filter(notice -> !notice.isDeleted())
                .orElseThrow(() -> new IllegalArgumentException("해당 공지사항이 존재하지 않습니다."));
    }

}
