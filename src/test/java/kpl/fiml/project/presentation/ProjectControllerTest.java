package kpl.fiml.project.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import kpl.fiml.TestDataFactory;
import kpl.fiml.customMockUser.WithCustomMockUser;
import kpl.fiml.project.domain.*;
import kpl.fiml.project.domain.enums.ProjectCategory;
import kpl.fiml.project.domain.enums.ProjectStatus;
import kpl.fiml.project.dto.request.*;
import kpl.fiml.user.domain.User;
import kpl.fiml.user.domain.UserRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static kpl.fiml.TestDataFactory.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectImageRepository projectImageRepository;

    @Autowired
    private ProjectLikeRepository projectLikeRepository;

    @Autowired
    private RewardRepository rewardRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        projectImageRepository.deleteAll();
        projectLikeRepository.deleteAll();
        rewardRepository.deleteAll();
        projectRepository.deleteAll();
        userRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        projectImageRepository.deleteAll();
        projectLikeRepository.deleteAll();
        rewardRepository.deleteAll();
        projectRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @WithCustomMockUser
    @DisplayName("프로젝트 초기화 테스트")
    void testInitProject() throws Exception {
        // Given
        userRepository.save(generateLoginUser());
        ProjectInitRequest request = ProjectInitRequest.builder()
                .summary("Project Summary")
                .category(ProjectCategory.BOARD_GAME)
                .build();

        // When
        mockMvc.perform(post("/api/v1/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.id").isNotEmpty());

        // Then
        assertThat(projectRepository.count()).isEqualTo(1);
    }

    @Test
    @WithCustomMockUser
    @Transactional
    @DisplayName("프로젝트 기본 정보 업데이트 테스트")
    void testUpdateBasicInfo() throws Exception {
        // Given
        User loginUser = userRepository.save(generateLoginUser());
        Project savedProject = projectRepository.save(TestDataFactory.generateDefaultProject(loginUser.getId()));

        ProjectBasicInfoUpdateRequest request = ProjectBasicInfoUpdateRequest.builder()
                .summary("Updated Summary")
                .category(ProjectCategory.BOARD_GAME)
                .title("Updated Title")
                .projectImages(List.of(
                        ProjectImageRequest.builder()
                                .path("https://sample-image1.com")
                                .build(),
                        ProjectImageRequest.builder()
                                .path("https://sample-image2.com")
                                .build()
                ))
                .build();

        // When
        mockMvc.perform(patch("/api/v1/projects/{projectId}/basic-info", savedProject.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk());

        // Then
        Project updatedProject = projectRepository.findById(savedProject.getId()).orElseThrow();
        assertThat(updatedProject.getSummary()).isEqualTo(request.getSummary());
        assertThat(updatedProject.getCategory()).isEqualTo(request.getCategory());
        assertThat(updatedProject.getTitle()).isEqualTo(request.getTitle());
        assertThat(updatedProject.getProjectImages()).hasSize(request.getProjectImages().size());
    }

    @Test
    @WithCustomMockUser
    @DisplayName("프로젝트 소개 정보 업데이트 테스트")
    void testUpdateIntroduction() throws Exception {
        // Given
        User loginUser = userRepository.save(generateLoginUser());
        Project savedProject = projectRepository.save(TestDataFactory.generateDefaultProject(loginUser.getId()));

        ProjectDetailIntroductionUpdateRequest request = ProjectDetailIntroductionUpdateRequest.builder()
                .introduction("Updated Introduction")
                .build();

        // When
        mockMvc.perform(patch("/api/v1/projects/{projectId}/introduction", savedProject.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk());

        // Then
        Project updatedProject = projectRepository.findById(savedProject.getId()).orElseThrow();
        assertThat(updatedProject.getIntroduction()).isEqualTo(request.getIntroduction());
    }

    @Test
    @WithCustomMockUser
    @DisplayName("프로젝트 펀딩 계획 업데이트 테스트")
    void testUpdateFundingPlan() throws Exception {
        // given
        User loginUser = userRepository.save(generateLoginUser());
        Project savedProject = projectRepository.save(TestDataFactory.generateDefaultProject(loginUser.getId()));
        ProjectFundingPlanUpdateRequest request = ProjectFundingPlanUpdateRequest.builder()
                .goalAmount(50000L)
                .fundingStartDateTime(LocalDateTime.now().plusDays(7))
                .fundingEndDate(LocalDate.now().plusDays(14))
                .commissionRate(5.0)
                .build();

        // When
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        mockMvc.perform(patch("/api/v1/projects/{projectId}/funding-plan", savedProject.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        // Then
        Project updatedProject = projectRepository.findById(savedProject.getId()).orElseThrow();
        assertThat(updatedProject.getGoalAmount()).isEqualTo(request.getGoalAmount());
        assertThat(updatedProject.getStartAt()).isCloseTo(request.getFundingStartDateTime(), within(1, ChronoUnit.SECONDS));
        assertThat(updatedProject.getEndAt()).isEqualTo(request.getFundingEndDate().atTime(23, 59, 59));
        assertThat(updatedProject.getCommissionRate()).isEqualTo(request.getCommissionRate());
    }

    @Test
    @WithCustomMockUser
    @Transactional
    @DisplayName("프로젝트 리워드 업데이트 테스트")
    void testUpdateRewards() throws Exception {
        // Given
        User loginUser = userRepository.save(generateLoginUser());
        Project savedProject = projectRepository.save(TestDataFactory.generateDefaultProject(loginUser.getId()));
        ProjectRewardUpdateRequest request = ProjectRewardUpdateRequest.builder()
                .rewards(List.of(
                        ProjectRewardRequest.builder()
                                .title("Reward 1")
                                .content("Reward 1 Description")
                                .price(10000L)
                                .quantityLimited(true)
                                .quantity(100)
                                .deliveryDate(LocalDate.now().plusDays(7))
                                .maxPurchaseQuantity(10)
                                .build(),
                        ProjectRewardRequest.builder()
                                .title("Reward 2")
                                .content("Reward 2 Description")
                                .price(20000L)
                                .quantityLimited(false)
                                .quantity(0)
                                .deliveryDate(LocalDate.now().plusDays(14))
                                .maxPurchaseQuantity(10)
                                .build()
                ))
                .build();


        // When
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        mockMvc.perform(patch("/api/v1/projects/{projectId}/rewards", savedProject.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        // Then
        Project updatedProject = projectRepository.findById(savedProject.getId()).orElseThrow();
        assertThat(updatedProject.getRewards()).hasSize(request.getRewards().size());
        assertThat(updatedProject.getRewards().get(0).getTitle()).isEqualTo(request.getRewards().get(0).getTitle());
        assertThat(updatedProject.getRewards().get(0).getContent()).isEqualTo(request.getRewards().get(0).getContent());
        assertThat(updatedProject.getRewards().get(0).getPrice()).isEqualTo(request.getRewards().get(0).getPrice());
        assertThat(updatedProject.getRewards().get(0).getQuantityLimited()).isEqualTo(request.getRewards().get(0).getQuantityLimited());
        assertThat(updatedProject.getRewards().get(0).getTotalQuantity()).isEqualTo(request.getRewards().get(0).getQuantity());
        assertThat(updatedProject.getRewards().get(0).getDeliveryDate()).isEqualTo(request.getRewards().get(0).getDeliveryDate());
        assertThat(updatedProject.getRewards().get(0).getMaxPurchaseQuantity()).isEqualTo(request.getRewards().get(0).getMaxPurchaseQuantity());
    }

    @Test
    @WithCustomMockUser
    @Transactional
    @DisplayName("프로젝트 제출 테스트")
    void testSubmitProject() throws Exception {
        // Given
        User loginUser = userRepository.save(generateLoginUser());
        Project savedProject = projectRepository.save(generateFullParamsProject(loginUser.getId()));

        // When
        mockMvc.perform(post("/api/v1/projects/{projectId}/submit", savedProject.getId()))
                .andExpect(status().isOk());

        // Then
        Project updatedProject = projectRepository.findById(savedProject.getId()).orElseThrow();
        assertThat(updatedProject.getStatus()).isEqualTo(ProjectStatus.PREPARING);
    }

    @Test
    @WithCustomMockUser
    @DisplayName("프로젝트 좋아요 등록 테스트")
    void testLikeProject() throws Exception {
        // Given
        User loginUser = userRepository.save(generateLoginUser());
        Project savedProject = projectRepository.save(generateFullParamsProject(loginUser.getId()));

        // When
        mockMvc.perform(post("/api/v1/projects/{projectId}/like", savedProject.getId()))
                .andExpect(status().isOk());

        // Then
        Project updatedProject = projectRepository.findById(savedProject.getId()).orElseThrow();
        assertThat(updatedProject.getLikedCount()).isEqualTo(1);
    }

    @Test
    @WithCustomMockUser
    @Transactional
    @DisplayName("프로젝트 좋아요 해제 테스트")
    void testUnlikeProject() throws Exception {
        // Given
        User loginUser = userRepository.save(generateLoginUser());
        Project savedProject = projectRepository.save(generateFullParamsProject(loginUser.getId()));
        projectLikeRepository.save(ProjectLike.builder()
                .project(savedProject)
                .user(savedProject.getUser())
                .build());
        projectRepository.increaseLikeCount(savedProject.getId());

        // When
        mockMvc.perform(post("/api/v1/projects/{projectId}/unlike", savedProject.getId()))
                .andExpect(status().isOk());

        // Then
        Project updatedProject = projectRepository.findById(savedProject.getId()).orElseThrow();
        assertThat(updatedProject.getLikedCount()).isZero();
    }

    @Test
    @WithCustomMockUser
    @DisplayName("프로젝트 삭제 테스트")
    void testDeleteProject() throws Exception {
        // Given
        User loginUser = userRepository.save(generateLoginUser());
        Project savedProject = projectRepository.save(generateDefaultProject(loginUser.getId()));

        // When
        mockMvc.perform(delete("/api/v1/projects/{projectId}", savedProject.getId()))
                .andExpect(status().isOk());

        // Then
        assertThat(projectRepository.findByIdWithUser(savedProject.getId())).isEmpty();
    }

    @ParameterizedTest
    @CsvSource({
            "0, 10",
            "0, 30",
            "1, 20",
            "2, 10",
            "3, 10",
    })
    @DisplayName("프로젝트 페이지네이션 조회 테스트")
    void testPagination(int page, int size) throws Exception {
        final Long userId = 1L;
        // Given
        User loginUser = userRepository.save(generateUserWithId(userId));
        List<Project> projectList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Project toSaveProject = generateFullParamsProject(loginUser.getId());
            ReflectionTestUtils.setField(toSaveProject, "status", ProjectStatus.PROCEEDING);
            projectList.add(
                    projectRepository.save(toSaveProject)
            );
        }

        // When
        projectList.sort((o1, o2) -> o2.getId().compareTo(o1.getId())); // 리스트 조회 시 기본 정렬 값은 id 내림차순
        final int index = page * size;
        Project firstProject = projectList.get(index);
        mockMvc.perform(get("/api/v1/projects")
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", Matchers.hasSize(size)))
                .andExpect(jsonPath("$.content[0].id").value(firstProject.getId()));
    }

    @Test
    @DisplayName("프로젝트 상세 조회 테스트")
    void testGetProjectDetail() throws Exception {
        final Long userId = 1L;
        // Given
        User loginUser = userRepository.save(generateUserWithId(userId));
        Project toSaveProject = generateFullParamsProject(loginUser.getId());
        ReflectionTestUtils.setField(toSaveProject, "status", ProjectStatus.PROCEEDING);
        Project savedProject = projectRepository.save(toSaveProject);

        // When
        mockMvc.perform(get("/api/v1/projects/{projectId}", savedProject.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedProject.getId()))
                .andExpect(jsonPath("$.title").value(savedProject.getTitle()))
                .andExpect(jsonPath("$.summary").value(savedProject.getSummary()));
    }
}