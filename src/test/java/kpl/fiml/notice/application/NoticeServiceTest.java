package kpl.fiml.notice.application;

import kpl.fiml.TestDataFactory;
import kpl.fiml.notice.domain.Notice;
import kpl.fiml.notice.domain.NoticeRepository;
import kpl.fiml.notice.dto.request.NoticeCreateRequest;
import kpl.fiml.notice.dto.response.NoticeCreateResponse;
import kpl.fiml.notice.exception.NoticePermissionException;
import kpl.fiml.project.application.ProjectService;
import kpl.fiml.project.domain.Project;
import kpl.fiml.user.application.UserService;
import kpl.fiml.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NoticeServiceTest {

    @InjectMocks
    private NoticeService noticeService;

    @Mock
    private UserService userService;

    @Mock
    private ProjectService projectService;

    @Mock
    private NoticeRepository noticeRepository;

    @Test
    @DisplayName("공지사항 생성에 성공합니다.")
    void testCreateNotice_Success() {
        // Given
        Long userId = 1L;
        Long projectId = 2L;

        User user = TestDataFactory.generateUserWithId(userId);
        Project project = TestDataFactory.generateDefaultProject(userId);
        ReflectionTestUtils.setField(project, "id", projectId);

        NoticeCreateRequest request = NoticeCreateRequest.builder()
                .content("Notice Content")
                .projectId(projectId)
                .build();

        when(userService.getById(userId)).thenReturn(user);
        when(projectService.getProjectByIdWithUser(projectId)).thenReturn(project);
        when(noticeRepository.save(any(Notice.class))).thenAnswer(invocation -> {
            Notice savedNotice = invocation.getArgument(0);
            ReflectionTestUtils.setField(savedNotice, "id", 3L);
            return savedNotice;
        });

        // When
        NoticeCreateResponse response = noticeService.createNotice(userId, request);

        // Then
        assertNotNull(response);
        assertEquals(3L, response.getId());
        assertEquals(userId, response.getUserId());
        assertEquals(projectId, response.getProjectId());

        // Verify
        verify(userService, times(1)).getById(userId);
        verify(projectService, times(1)).getProjectByIdWithUser(projectId);
        verify(noticeRepository, times(1)).save(any(Notice.class));
    }

    @Test
    @DisplayName("공지사항 생성 실패 : 접근 권한 없음")
    void testCreateNotice_Fail_AccessDenied() {
        // Given
        Long userId = 1L;
        Long projectId = 2L;

        User user = TestDataFactory.generateUserWithId(userId);
        Project project = TestDataFactory.generateDefaultProject(projectId);

        NoticeCreateRequest request = NoticeCreateRequest.builder()
                .content("Notice Content")
                .projectId(projectId)
                .build();

        when(userService.getById(userId)).thenReturn(user);
        when(projectService.getProjectByIdWithUser(projectId)).thenReturn(project);

        // When/Then
        assertThrows(NoticePermissionException.class, () -> noticeService.createNotice(userId, request));

        // Verify
        verify(userService, times(1)).getById(userId);
        verify(projectService, times(1)).getProjectByIdWithUser(projectId);
        verify(noticeRepository, never()).save(any(Notice.class));
    }
}