package kpl.fiml.notice.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import kpl.fiml.TestDataFactory;
import kpl.fiml.customMockUser.WithCustomMockUser;
import kpl.fiml.notice.domain.NoticeRepository;
import kpl.fiml.notice.dto.request.NoticeCreateRequest;
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

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class NoticeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private NoticeRepository noticeRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        noticeRepository.deleteAll();
        projectRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @WithCustomMockUser
    @DisplayName("공지사항 작성 테스트")
    void testNoticeCreate_Success() throws Exception {
        // Given
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User loginUser = (User) authentication.getPrincipal();

        User user = userRepository.save(loginUser);
        Project project = TestDataFactory.generateDefaultProject(user.getId());
        Project savedProject = projectRepository.save(project);

        NoticeCreateRequest request = NoticeCreateRequest.builder()
                .content("Sample notice content")
                .projectId(savedProject.getId())
                .build();


        // When, Then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/notices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.userId").value(project.getUser().getId()))
                .andExpect(jsonPath("$.projectId").value(project.getId()));
    }
}
