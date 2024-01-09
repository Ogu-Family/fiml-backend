package kpl.fiml.comment.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import kpl.fiml.TestDataFactory;
import kpl.fiml.comment.domain.Comment;
import kpl.fiml.comment.domain.CommentRepository;
import kpl.fiml.comment.dto.request.CommentCreateRequest;
import kpl.fiml.comment.dto.request.CommentUpdateRequest;
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

    @Test
    @WithCustomMockUser
    @DisplayName("댓글을 수정 합니다.")
    void testUpdateComment_Success() throws Exception {
        // Given
        User loginUser = create_loginUser();
        User user = userRepository.save(TestDataFactory.generateUserWithId(0L));
        Project project = projectRepository.save(TestDataFactory.generateDefaultProject(user.getId()));
        Notice notice = noticeRepository.save(TestDataFactory.generateNotice(user, project));
        Comment comment = commentRepository.save(TestDataFactory.generateComment(loginUser, notice));

        CommentUpdateRequest request = CommentUpdateRequest.builder()
                .content("Updated Comment Content").build();

        // When, Then
        mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/comments/{commentId}", comment.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId").value(loginUser.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").value(request.getContent()));
    }

    @Test
    @DisplayName("댓글 조회 : 공지사항 기준")
    void testFindAllCommentsByNoticeId_Success() throws Exception {
        // Given
        User user = userRepository.save(TestDataFactory.generateUserWithId(0L));
        Project project = projectRepository.save(TestDataFactory.generateDefaultProject(user.getId()));
        Notice notice = noticeRepository.save(TestDataFactory.generateNotice(user, project));
        Comment comment1 = commentRepository.save(TestDataFactory.generateComment(user, notice));
        Comment comment2 = commentRepository.save(TestDataFactory.generateComment(user, notice));

        // When, Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/notice/{noticeId}/comments", notice.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].userId").value(user.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].noticeId").value(notice.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].content").value(comment1.getContent()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].userId").value(user.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].noticeId").value(notice.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].content").value(comment2.getContent()));
    }

    @Test
    @WithCustomMockUser
    @DisplayName("댓글을 삭제 합니다.")
    void testDeleteComment_Success() throws Exception {
        // Given
        User loginUser = create_loginUser();
        User user = userRepository.save(TestDataFactory.generateUserWithId(0L));
        Project project = projectRepository.save(TestDataFactory.generateDefaultProject(user.getId()));
        Notice notice = noticeRepository.save(TestDataFactory.generateNotice(user, project));
        Comment comment = commentRepository.save(TestDataFactory.generateComment(loginUser, notice));

        // When, Then
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/comments/{id}", comment.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(comment.getId()));
    }

    private User create_loginUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User loginUser = (User) authentication.getPrincipal();

        return userRepository.save(loginUser);
    }
}