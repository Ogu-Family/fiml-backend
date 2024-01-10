package kpl.fiml.comment.application;

import kpl.fiml.TestDataFactory;
import kpl.fiml.comment.domain.Comment;
import kpl.fiml.comment.domain.CommentRepository;
import kpl.fiml.comment.dto.request.CommentCreateRequest;
import kpl.fiml.comment.dto.response.CommentCreateResponse;
import kpl.fiml.notice.application.NoticeService;
import kpl.fiml.notice.domain.Notice;
import kpl.fiml.project.domain.Project;
import kpl.fiml.user.application.UserService;
import kpl.fiml.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @InjectMocks
    private CommentService commentService;

    @Mock
    private UserService userService;

    @Mock
    private NoticeService noticeService;

    @Mock
    private CommentRepository commentRepository;

    @Test
    @DisplayName("댓글 생성에 성공 합니다.")
    void testCreateComment_Success() {
        // Given
        Long userId = 1L;
        Long noticeId = 2L;

        User user = TestDataFactory.generateUserWithId(userId);
        Project project = TestDataFactory.generateDefaultProject(userId);
        Notice notice = TestDataFactory.generateNotice(user, project);
        CommentCreateRequest request = CommentCreateRequest.builder()
                .content("Comment Content")
                .build();

        when(userService.getById(userId)).thenReturn(user);
        when(noticeService.getById(noticeId)).thenReturn(notice);

        Comment createdComment = Comment.builder()
                .content(request.getContent())
                .user(user)
                .notice(notice)
                .build();
        when(commentRepository.save(any(Comment.class))).thenReturn(createdComment);

        // When
        CommentCreateResponse commentCreateResponse = commentService.create(userId, noticeId, request);

        // Then
        assertNotNull(commentCreateResponse);
        assertEquals(createdComment.getId(), commentCreateResponse.getId());
        assertEquals(createdComment.getContent(), commentCreateResponse.getContent());
        assertEquals(userId, commentCreateResponse.getUserId());
        assertEquals(noticeId, commentCreateResponse.getNoticeId());

        // Verify
        verify(userService, times(1)).getById(userId);
        verify(noticeService, times(1)).getById(noticeId);
        verify(commentRepository, times(1)).save(any(Comment.class));
    }
}