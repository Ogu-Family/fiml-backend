package kpl.fiml.comment.application;

import kpl.fiml.comment.domain.Comment;
import kpl.fiml.comment.domain.CommentRepository;
import kpl.fiml.comment.dto.*;
import kpl.fiml.notice.domain.Notice;
import kpl.fiml.notice.domain.NoticeRepository;
import kpl.fiml.user.domain.User;
import kpl.fiml.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {

    private final UserRepository userRepository;  // TODO : Service 단에서 끌고 오기
    private final NoticeRepository noticeRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public CommentCreateResponse create(Long userId, Long noticeId, CommentCreateRequest request) {
        User user = getUserByUserId(userId);
        Notice notice = getNoticeById(noticeId);
        Comment createdComment = commentRepository.save(request.toEntity(user, notice));

        return CommentCreateResponse.of(createdComment.getId(), createdComment.getContent(), userId, noticeId);
    }

    public List<CommentDto> findAllByNoticeId(Long noticeId) {
        List<Comment> findList = getNoticeById(noticeId).getCommentList();

        return findList.stream()
                .filter(comment -> !comment.isDeleted())
                .map(comment -> CommentDto.of(comment.getId(), comment.getContent(), comment.getUser().getId(), noticeId, comment.getCreatedAt(), comment.getUpdatedAt()))
                .toList();
    }

    @Transactional
    public CommentUpdateResponse update(Long id, Long userId, CommentUpdateRequest request) {
        Comment findComment = getById(id);
        User user = getUserByUserId(userId);

        if (!user.isSameUser(findComment.getUser())) {
            throw new IllegalArgumentException("댓글 작성자만 댓글 수정이 가능합니다.");
        }
        findComment.updateContent(request.getContent());

        return CommentUpdateResponse.of(findComment.getId(), userId, findComment.getContent(), findComment.getCreatedAt(), findComment.getUpdatedAt());
    }

    @Transactional
    public CommentDeleteResponse deleteById(Long id, Long userId) {
        Comment findComment = getById(id);
        User user = getUserByUserId(userId);

        if (!user.isSameUser(findComment.getUser())) {
            throw new IllegalArgumentException("댓글 작성자만 댓글 삭제가 가능합니다.");
        }
        findComment.delete();

        return CommentDeleteResponse.of(findComment.getId());
    }

    private Comment getById(Long id) {
        return commentRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다."));
    }

    private User getUserByUserId(Long userId) {
        return userRepository.findByIdAndDeletedAtIsNull(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));
    }

    private Notice getNoticeById(Long noticeId) {
        return noticeRepository.findByIdAndDeletedAtIsNull(noticeId)
                .orElseThrow(() -> new IllegalArgumentException("해당 공지사항이 존재하지 않습니다."));
    }
}
