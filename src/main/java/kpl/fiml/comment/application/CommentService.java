package kpl.fiml.comment.application;

import kpl.fiml.comment.domain.Comment;
import kpl.fiml.comment.domain.CommentRepository;
import kpl.fiml.comment.dto.*;
import kpl.fiml.notice.domain.Notice;
import kpl.fiml.notice.domain.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    @Transactional(readOnly = true)
    public List<CommentDto> findAllByNoticeId(Long noticeId) {
        List<Comment> findList = getNoticeById(noticeId).getCommentList();

        return findList.stream()
                .filter(comment -> !comment.isDeleted())
                .map(comment -> CommentDto.of(comment.getId(), comment.getContent(), comment.getCommenter(), noticeId, comment.getCreatedAt(), comment.getUpdatedAt()))
                .toList();
    }

    @Transactional
    public CommentUpdateResponse update(Long id, CommentUpdateRequest request) {
        Comment findComment = getById(id);
        findComment.updateContent(request.getContent());

        return CommentUpdateResponse.of(findComment.getId(), findComment.getContent(), findComment.getCommenter(), findComment.getCreatedAt(), findComment.getUpdatedAt());
    }

    @Transactional
    public CommentDeleteResponse deleteById(Long id) {
        Comment findComment = getById(id);
        findComment.delete();

        return CommentDeleteResponse.of(findComment.getId());
    }

    private Comment getById(Long id) {
        return commentRepository.findById(id)
                .filter(comment -> !comment.isDeleted())
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다."));
    }
    private Notice getNoticeById(Long noticeId) {
        return noticeRepository.findById(noticeId)
                .filter(notice -> !notice.isDeleted())
                .orElseThrow(() -> new IllegalArgumentException("해당 공지사항이 존재하지 않습니다."));
    }
}
