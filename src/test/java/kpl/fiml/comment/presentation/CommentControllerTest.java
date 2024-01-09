package kpl.fiml.comment.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import kpl.fiml.TestDataFactory;
import kpl.fiml.comment.domain.CommentRepository;
import kpl.fiml.comment.dto.request.CommentCreateRequest;
import kpl.fiml.customMockUser.WithCustomMockUser;
import kpl.fiml.notice.domain.Notice;
import kpl.fiml.notice.domain.NoticeRepository;
import kpl.fiml.project.domain.Project;
import kpl.fiml.project.domain.ProjectRepository;
import kpl.fiml.user.domain.User;
import kpl.fiml.user.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
class CommentControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private NoticeRepository noticeRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @BeforeEach
    void setUp() {
        commentRepository.deleteAll();
        noticeRepository.deleteAll();
        projectRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @WithCustomMockUser
    @DisplayName("댓글을 생성합니다.")
    void testCreateComment_Success() throws Exception {
        // Given
        User loginUser = create_loginUser();
        User user = userRepository.save(TestDataFactory.generateUserWithId(0L));
        Project project = projectRepository.save(TestDataFactory.generateDefaultProject(user.getId()));
        Notice notice = noticeRepository.save(TestDataFactory.generateNotice(user, project));

        CommentCreateRequest request = CommentCreateRequest.builder()
                .content("Sample Comment Content").build();

        // When, Then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/notice/{noticeId}/comments", notice.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId").value(loginUser.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.noticeId").value(notice.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").value(request.getContent()));
    }

    private User create_loginUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User loginUser = (User) authentication.getPrincipal();

        return userRepository.save(loginUser);
    }
}