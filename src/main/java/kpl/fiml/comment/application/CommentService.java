package kpl.fiml.comment.application;

import kpl.fiml.comment.domain.Comment;
import kpl.fiml.comment.domain.CommentRepository;
import kpl.fiml.comment.dto.*;
import kpl.fiml.comment.dto.request.CommentCreateRequest;
import kpl.fiml.comment.dto.request.CommentUpdateRequest;
import kpl.fiml.comment.dto.response.CommentCreateResponse;
import kpl.fiml.comment.dto.response.CommentDeleteResponse;
import kpl.fiml.comment.dto.response.CommentUpdateResponse;
import kpl.fiml.comment.exception.CommentErrorCode;
import kpl.fiml.comment.exception.CommentNotFoundException;
import kpl.fiml.notice.application.NoticeService;
import kpl.fiml.notice.domain.Notice;
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
public class CommentService {

    private final UserService userService;
    private final NoticeService noticeService;
    private final CommentRepository commentRepository;

    @Transactional
    public CommentCreateResponse create(Long userId, Long noticeId, CommentCreateRequest request) {
        User user = userService.getById(userId);
        Notice notice = noticeService.getById(noticeId);
        Comment createdComment = commentRepository.save(request.toEntity(user, notice));

        return CommentCreateResponse.of(createdComment.getId(), createdComment.getContent(), userId, noticeId);
    }

    public List<CommentDto> findAllByNoticeId(Long noticeId) {
        List<Comment> findList = commentRepository.findAllByNoticeIdAndDeletedAtIsNull(noticeId).get();

        return findList.stream()
                .map(comment -> CommentDto.of(comment.getId(), comment.getContent(), comment.getUser().getId(), noticeId, comment.getCreatedAt(), comment.getUpdatedAt()))
                .toList();
    }

    @Transactional
    public CommentUpdateResponse update(Long id, Long userId, CommentUpdateRequest request) {
        Comment findComment = getById(id);
        User user = userService.getById(userId);

        findComment.updateContent(Objects.requireNonNull(request.getContent(), ""), user);

        return CommentUpdateResponse.of(findComment.getId(), userId, findComment.getContent(), findComment.getCreatedAt(), findComment.getUpdatedAt());
    }

    @Transactional
    public CommentDeleteResponse deleteById(Long id, Long userId) {
        Comment findComment = getById(id);
        User user = userService.getById(userId);

        findComment.deleteComment(user);

        return CommentDeleteResponse.of(findComment.getId());
    }

    public Comment getById(Long id) {
        return commentRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new CommentNotFoundException(CommentErrorCode.COMMENT_NOT_FOUND));
    }
}
