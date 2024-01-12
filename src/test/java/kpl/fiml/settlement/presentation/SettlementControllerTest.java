package kpl.fiml.settlement.presentation;

import kpl.fiml.customMockUser.WithCustomMockUser;
import kpl.fiml.project.domain.Project;
import kpl.fiml.project.domain.ProjectRepository;
import kpl.fiml.project.domain.enums.ProjectCategory;
import kpl.fiml.project.domain.enums.ProjectStatus;
import kpl.fiml.settlement.domain.Settlement;
import kpl.fiml.settlement.domain.SettlementRepository;
import kpl.fiml.user.domain.User;
import kpl.fiml.user.domain.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class SettlementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private SettlementRepository settlementRepository;

    @BeforeEach
    void setUp() {
        settlementRepository.deleteAll();
        projectRepository.deleteAll();
        userRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        settlementRepository.deleteAll();
        projectRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @WithCustomMockUser
    @DisplayName("특정 프로젝트에 대한 정산 정보를 조회할 수 있다.")
    void testGetSettlementByProject() throws Exception {
        // given
        User user = userRepository.save(createUser());
        Project project = projectRepository.save(createProject(user));
        Settlement settlement = settlementRepository.save(createSettlement(project));

        // when-then
        mockMvc.perform(get("/api/v1/settlements/{projectId}", project.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.settleAmount").value(settlement.getSettleAmount()))
                .andExpect(jsonPath("$.settledAt").isNotEmpty());
    }

    User createUser() {
        return (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
    }

    Project createProject(User user) {
        Project project = Project.builder()
                .summary("summary")
                .category(ProjectCategory.ART)
                .user(user)
                .build();
        ReflectionTestUtils.setField(project, "endAt", LocalDateTime.now().plusDays(1L));
        ReflectionTestUtils.setField(project, "status", ProjectStatus.PROCEEDING);

        return project;
    }

    Settlement createSettlement(Project project) {
        return Settlement.builder()
                .project(project)
                .settleAmount(123456L)
                .settledAt(LocalDateTime.now())
                .build();
    }
}
