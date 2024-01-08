package kpl.fiml.project.application;

import kpl.fiml.project.domain.Project;
import kpl.fiml.project.domain.ProjectLike;
import kpl.fiml.project.domain.ProjectLikeRepository;
import kpl.fiml.project.domain.ProjectRepository;
import kpl.fiml.project.domain.enums.ProjectCategory;
import kpl.fiml.project.domain.enums.ProjectStatus;
import kpl.fiml.project.dto.request.*;
import kpl.fiml.project.dto.response.ProjectInitResponse;
import kpl.fiml.user.application.UserService;
import kpl.fiml.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static kpl.fiml.TestDataFactory.generateDefaultProject;
import static kpl.fiml.TestDataFactory.generateFullParamsProject;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private ProjectLikeRepository projectLikeRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private ProjectService projectService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Project 초기화 시 Project를 생성하고, 생성된 Project의 ID를 반환합니다.")
    void testInitProject_Success() {
        final Long userId = 1L;
        final Long mockProjectId = 1L;
        // Given
        Project project = generateDefaultProject(userId);
        ReflectionTestUtils.setField(project, "id", mockProjectId);
        ProjectInitRequest request = ProjectInitRequest
                .builder()
                .summary(project.getTitle())
                .category(project.getCategory())
                .build();

        when(userService.getById(userId)).thenReturn(project.getUser());
        when(projectRepository.save(any())).thenReturn(project);

        // When
        ProjectInitResponse response = projectService.initProject(request, userId);

        // Then
        verify(userService, times(1)).getById(userId);
        verify(projectRepository, times(1)).save(any());
        assertThat(response.getId()).isEqualTo(project.getId());
    }

    @Test
    @DisplayName("Project 기본 정보 업데이트 시 유저와 프로젝트를 조회하면서 업데이트를 진행합니다.")
    void testUpdateBasicInfo_Success() {
        final Long projectId = 1L;
        final Long userId = 1L;
        // Given
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

        Project project = generateDefaultProject(userId);
        User user = project.getUser();

        when(userService.getById(userId)).thenReturn(user);
        when(projectRepository.findByIdWithUser(projectId)).thenReturn(Optional.of(project));

        // When
        projectService.updateBasicInfo(projectId, request, userId);

        // Then
        verify(userService, times(1)).getById(userId);
        verify(projectRepository, times(1)).findByIdWithUser(projectId);
        assertThat(project.getSummary()).isEqualTo(request.getSummary());
        assertThat(project.getCategory()).isEqualTo(request.getCategory());
        assertThat(project.getTitle()).isEqualTo(request.getTitle());
        assertThat(project.getProjectImages()).hasSize(2);
    }

    @Test
    @DisplayName("Project 소개 업데이트 시 유저와 프로젝트를 조회하면서 업데이트를 진행합니다.")
    void testUpdateIntroduction_Success() {
        final Long projectId = 1L;
        final Long userId = 1L;
        // Given
        ProjectDetailIntroductionUpdateRequest request = ProjectDetailIntroductionUpdateRequest.builder()
                .introduction("Updated Introduction")
                .build();

        Project project = generateDefaultProject(userId);
        User user = project.getUser();

        when(userService.getById(userId)).thenReturn(user);
        when(projectRepository.findByIdWithUser(projectId)).thenReturn(Optional.of(project));

        // When
        projectService.updateIntroduction(projectId, request, userId);

        // Then
        verify(userService, times(1)).getById(userId);
        verify(projectRepository, times(1)).findByIdWithUser(projectId);
        assertThat(project.getIntroduction()).isEqualTo(request.getIntroduction());
    }

    @Test
    @DisplayName("Project 펀딩 계획 업데이트 시 유저와 프로젝트를 조회하면서 업데이트를 진행합니다.")
    void testUpdateFundingPlan_Success() {
        final Long projectId = 1L;
        final Long userId = 1L;
        // Given
        ProjectFundingPlanUpdateRequest request = ProjectFundingPlanUpdateRequest.builder()
                .goalAmount(50000L)
                .fundingStartDateTime(LocalDateTime.now().plusDays(7))
                .fundingEndDate(LocalDate.now().plusDays(14))
                .commissionRate(5.0)
                .build();

        Project project = generateDefaultProject(userId);
        User user = project.getUser();

        when(userService.getById(userId)).thenReturn(user);
        when(projectRepository.findByIdWithUser(projectId)).thenReturn(Optional.of(project));

        // When
        projectService.updateFundingPlan(projectId, request, userId);

        // Then
        verify(userService, times(1)).getById(userId);
        verify(projectRepository, times(1)).findByIdWithUser(projectId);

        assertThat(project.getGoalAmount()).isEqualTo(request.getGoalAmount());
        assertThat(project.getStartAt()).isEqualTo(request.getFundingStartDateTime());
        assertThat(project.getEndAt()).isEqualTo(request.getFundingEndDate().atTime(23, 59, 59));
        assertThat(project.getCommissionRate()).isEqualTo(request.getCommissionRate());
    }

    @Test
    @DisplayName("Project 리워드 업데이트 시 유저와 프로젝트를 조회하면서 업데이트를 진행합니다.")
    void testUpdateReward_Success() {
        final Long projectId = 1L;
        final Long userId = 1L;
        // Given
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

        Project project = generateDefaultProject(userId);
        User user = project.getUser();
        when(userService.getById(userId)).thenReturn(user);
        when(projectRepository.findByIdWithUser(projectId)).thenReturn(Optional.of(project));

        // When
        projectService.updateRewards(projectId, request, userId);

        // Then
        verify(userService, times(1)).getById(userId);
        verify(projectRepository, times(1)).findByIdWithUser(projectId);
        assertThat(project.getRewards()).hasSize(2);
    }

    @Test
    @DisplayName("Project 제출 시 유저와 프로젝트를 조회하면서 제출을 진행합니다.")
    void testSubmitProject_Success() {
        final Long projectId = 1L;
        final Long userId = 1L;
        // Given
        Project project = generateFullParamsProject(userId); // Project 생성 시 모든 필드를 세팅한 상태로 생성
        User user = project.getUser();

        when(userService.getById(userId)).thenReturn(user);
        when(projectRepository.findByIdWithUser(projectId)).thenReturn(Optional.of(project));

        // When
        projectService.submitProject(projectId, userId);

        // Then
        verify(userService, times(1)).getById(userId);
        verify(projectRepository, times(1)).findByIdWithUser(projectId);
        assertThat(project.getStatus()).isEqualTo(ProjectStatus.PREPARING);
    }

    @Test
    @DisplayName("Project 좋아요 시 유저와 프로젝트를 조회하면서 좋아요를 진행합니다.")
    void testLikeProject_Success() {
        final Long projectId = 1L;
        final Long userId = 1L;
        // Given
        Project project = generateDefaultProject(userId);
        User user = project.getUser();

        when(userService.getById(userId)).thenReturn(user);
        when(projectRepository.findByIdWithUser(projectId)).thenReturn(Optional.of(project));

        // When
        projectService.likeProject(projectId, userId);

        // Then
        verify(userService, times(1)).getById(userId);
        verify(projectRepository, times(1)).findByIdWithUser(projectId);
        verify(projectLikeRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("이미 좋아요를 누른 프로젝트일 경우 예외를 발생시킵니다.")
    void testLikeProject_AlreadyLikedProject() {
        final Long projectId = 1L;
        final Long userId = 1L;
        // Given
        Project project = generateDefaultProject(userId);
        User user = project.getUser();

        when(userService.getById(userId)).thenReturn(user);
        when(projectRepository.findByIdWithUser(projectId)).thenReturn(Optional.of(project));
        when(projectLikeRepository.existsByProjectAndUser(project, user)).thenReturn(true);

        // When, Then
        assertThatThrownBy(() -> projectService.likeProject(projectId, userId))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Project 좋아요 취소 시 유저와 프로젝트를 조회하면서 좋아요 취소를 진행합니다.")
    void testUnlikeProject_Success() {
        final Long projectId = 1L;
        final Long userId = 1L;
        // Given
        Project project = generateDefaultProject(userId);
        User user = project.getUser();

        when(userService.getById(userId)).thenReturn(user);
        when(projectRepository.findByIdWithUser(projectId)).thenReturn(Optional.of(project));
        when(projectLikeRepository.findByProjectAndUser(project, user)).thenReturn(Optional.of(
                ProjectLike.builder()
                        .project(project)
                        .user(user)
                        .build()
        ));

        // When
        projectService.unlikeProject(projectId, userId);

        // Then
        verify(userService, times(1)).getById(userId);
        verify(projectRepository, times(1)).findByIdWithUser(projectId);
        verify(projectLikeRepository, times(1)).findByProjectAndUser(project, user);
        verify(projectLikeRepository, times(1)).delete(any());
    }

    @Test
    @DisplayName("좋아요를 누르지 않은 프로젝트일 경우 예외를 발생시킵니다.")
    void testUnlikeProject_NotLikedProject() {
        final Long projectId = 1L;
        final Long userId = 1L;
        // Given
        Project project = generateDefaultProject(userId);
        User user = project.getUser();

        when(userService.getById(userId)).thenReturn(user);
        when(projectRepository.findByIdWithUser(projectId)).thenReturn(Optional.of(project));
        when(projectLikeRepository.findByProjectAndUser(project, user)).thenReturn(Optional.empty());

        // When, Then
        assertThatThrownBy(() -> projectService.unlikeProject(projectId, userId))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Project 삭제 시 유저와 프로젝트를 조회하면서 삭제를 진행합니다.")
    void testDeleteProject_Success() {
        final Long projectId = 1L;
        final Long userId = 1L;
        // Given
        Project project = generateDefaultProject(userId);
        User user = project.getUser();

        when(userService.getById(userId)).thenReturn(user);
        when(projectRepository.findByIdWithUser(projectId)).thenReturn(Optional.of(project));

        // When
        projectService.deleteProject(projectId, userId);

        // Then
        verify(userService, times(1)).getById(userId);
        verify(projectRepository, times(1)).findByIdWithUser(projectId);
        assertThat(project.getStatus()).isEqualTo(ProjectStatus.CANCEL);
        assertThat(project.getDeletedAt()).isNotNull();
    }
}
