package kpl.fiml.notice.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import kpl.fiml.TestDataFactory;
import kpl.fiml.customMockUser.WithCustomMockUser;
import kpl.fiml.notice.domain.Notice;
import kpl.fiml.notice.domain.NoticeRepository;
import kpl.fiml.notice.dto.request.NoticeCreateRequest;
import kpl.fiml.notice.dto.request.NoticeUpdateRequest;
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

        User savedUser = userRepository.save(loginUser);
        Project savedProject = projectRepository.save(TestDataFactory.generateDefaultProject(savedUser.getId()));

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
                .andExpect(jsonPath("$.userId").value(savedProject.getUser().getId()))
                .andExpect(jsonPath("$.projectId").value(savedProject.getId()));
    }

    @Test
    @WithCustomMockUser
    @DisplayName("공지사항 수정 테스트")
    void testNoticeUpdate_Success() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User loginUser = (User) authentication.getPrincipal();

        User savedUser = userRepository.save(loginUser);
        Project savedProject = projectRepository.save(TestDataFactory.generateDefaultProject(savedUser.getId()));
        Notice savedNotice = noticeRepository.save(TestDataFactory.generateNotice(savedUser, savedProject));

        String updateContent = "Updated notice content";
        NoticeUpdateRequest request = NoticeUpdateRequest.builder()
                .content(updateContent).build();

        // When, Then
        mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/notices/{id}", savedNotice.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id").value(savedNotice.getId()))
                .andExpect(jsonPath("$.content").value(updateContent))
                .andExpect(jsonPath("$.userId").value(savedUser.getId()));
    }

}
