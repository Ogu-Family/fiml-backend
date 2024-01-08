package kpl.fiml.project.domain;

import kpl.fiml.project.domain.enums.ProjectCategory;
import kpl.fiml.project.domain.enums.ProjectStatus;
import kpl.fiml.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static kpl.fiml.TestDataFactory.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProjectTest {

    private static Stream<Arguments> provideUpdateFundingInfo() {
        return Stream.of(
                Arguments.of(50000L, LocalDateTime.now().plusDays(1), LocalDate.now().plusDays(30), 5.0),
                Arguments.of(100000L, LocalDateTime.now().plusDays(1), LocalDate.now().plusDays(30), 5.0),
                Arguments.of(50000L, LocalDateTime.now().plusDays(1), LocalDate.now().plusDays(30), 10.0),
                Arguments.of(100000L, LocalDateTime.now().plusDays(1), LocalDate.now().plusDays(30), 10.0)
        );
    }

    private static Stream<Arguments> provideInvalidCommissionRate() {
        return Stream.of(
                Arguments.of(-1.0),
                Arguments.of(-100.0),
                Arguments.of(101.0),
                Arguments.of(1000.0)
        );
    }

    private static Stream<Arguments> provideInvalidGoalAmount() {
        return Stream.of(
                Arguments.of(-1L),
                Arguments.of(-100L)
        );
    }

    private static Stream<Arguments> provideInvalidFundingDateTime() {
        return Stream.of(
                Arguments.of(LocalDateTime.now().plusDays(1), LocalDate.now().plusDays(0)),
                Arguments.of(LocalDateTime.now().plusDays(1), LocalDate.now()),
                Arguments.of(LocalDateTime.now(), LocalDate.now().plusDays(1)),
                Arguments.of(LocalDateTime.now().minusDays(1), LocalDate.now().plusDays(1))
        );
    }

    private static Stream<Arguments> provideInvalidProjectStatus() {
        return Stream.of(
                Arguments.of(ProjectStatus.FUNDING_COMPLETE),
                Arguments.of(ProjectStatus.FUNDING_FAILURE),
                Arguments.of(ProjectStatus.SETTLEMENT_COMPLETE)
        );
    }

    @Test
    @DisplayName("Project 객체 생성 시 Summary, Category, User 정보와 함께 기본 정보가 생성됩니다.")
    void testCreateProject_Success() {
        // Given
        Project project = generateDefaultProject(1L);

        // Then
        assertThat(project).isNotNull();
        assertThat(project.getSummary()).isNotNull();
        assertThat(project.getCategory()).isNotNull();
        assertThat(project.getUser()).isNotNull();

        assertThat(project.getCurrentAmount()).isZero();
        assertThat(project.getSharedCount()).isZero();
        assertThat(project.getLikedCount()).isZero();
        assertThat(project.getSponsorCount()).isZero();
        assertThat(project.getStatus()).isEqualTo(ProjectStatus.WRITING);
        assertThat(project.getProjectImages()).isEmpty();
        assertThat(project.getRewards()).isEmpty();
    }

    @Test
    @DisplayName("Project 객체 기본 정보 업데이트 시 변경된 정보가 저장됩니다.")
    void testUpdateBasicInfo_Success() {
        final Long userId = 1L;
        // Given
        Project project = generateDefaultProject(userId);
        User user = project.getUser();

        String newSummary = "Updated summary";
        ProjectCategory newCategory = ProjectCategory.BOARD_GAME;
        String newTitle = "Updated Title";

        List<ProjectImage> newProjectImages = generateSampleProjectImages();

        // When
        project.updateBasicInfo(newSummary, newCategory, newTitle, newProjectImages, user);

        // Then
        assertThat(project.getSummary()).isEqualTo(newSummary);
        assertThat(project.getCategory()).isEqualTo(newCategory);
        assertThat(project.getTitle()).isEqualTo(newTitle);
        assertThat(project.getProjectImages().get(0).getProject()).isEqualTo(project);
        assertThat(project.getProjectImages()).hasSize(newProjectImages.size());
    }

    @Test
    @DisplayName("Project 객체 소개 정보 업데이트 시 변경된 정보가 저장됩니다.")
    void testUpdateIntroduction_Success() {
        final Long userId = 1L;
        // Given
        Project project = generateDefaultProject(1L);
        User user = project.getUser();

        String newIntroduction = "Updated introduction";

        // When
        project.updateIntroduction(newIntroduction, user);

        // Then
        assertThat(project.getIntroduction()).isEqualTo(newIntroduction);
    }

    @ParameterizedTest
    @MethodSource("provideUpdateFundingInfo")
    @DisplayName("Project 객체 펀딩 정보에 올바른 값이 입력되면 변경된 정보가 저장됩니다.")
    void testUpdateFundingInfo_Success(Long goalAmount, LocalDateTime startDateTime, LocalDate endDate, Double commissionRate) {
        final Long userId = 1L;
        // Given
        Project project = generateDefaultProject(userId);
        User user = project.getUser();

        // When
        project.updateFundingInfo(goalAmount, startDateTime, endDate, commissionRate, user);

        // Then
        assertThat(project.getGoalAmount()).isEqualTo(goalAmount);
        assertThat(project.getStartAt()).isEqualTo(startDateTime);
        assertThat(project.getEndAt()).isEqualTo(endDate.atTime(23, 59, 59));
        assertThat(project.getCommissionRate()).isEqualTo(commissionRate);
    }

    @ParameterizedTest
    @MethodSource("provideInvalidCommissionRate")
    @DisplayName("Project 객체 펀딩 정보 업데이트 시 수수료율이 음수이거나 100 초과인 경우 예외가 발생합니다.")
    void testUpdateFundingInfo_NegativeCommissionRateException(Double negativeCommissionRate) {
        final Long userId = 1L;
        // Given
        Project project = generateDefaultProject(userId);
        User user = project.getUser();

        Long goalAmount = 50000L;
        LocalDateTime startDateTime = LocalDateTime.now().plusDays(1);
        LocalDate endDate = LocalDate.now().plusDays(30);

        // When, Then
        assertThatThrownBy(() -> project.updateFundingInfo(goalAmount, startDateTime, endDate, negativeCommissionRate, user))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @MethodSource("provideInvalidGoalAmount")
    @DisplayName("Project 객체 펀딩 정보 업데이트 시 목표 금액이 음수인 경우 예외가 발생합니다.")
    void testUpdateFundingInfo_NegativeGoalAmountException(Long negativeGoalAmount) {
        final Long userId = 1L;
        // Given
        Project project = generateDefaultProject(userId);
        User user = project.getUser();

        LocalDateTime startDateTime = LocalDateTime.now().plusDays(1);
        LocalDate endDate = LocalDate.now().plusDays(30);
        Double commissionRate = 5.0;

        // When, Then
        assertThatThrownBy(() -> project.updateFundingInfo(negativeGoalAmount, startDateTime, endDate, commissionRate, user))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @MethodSource("provideInvalidFundingDateTime")
    @DisplayName("Project 객체 펀딩 정보 업데이트 시 올바르지 않은 펀딩 시작일 또는 종료일인 경우 예외가 발생합니다.")
    void testUpdateFundingInfo_StartDateAfterEndDateException(LocalDateTime invalidStartDateTime, LocalDate invalidEndDate) {
        final Long userId = 1L;
        // Given
        Project project = generateDefaultProject(userId);
        User user = project.getUser();

        Long goalAmount = 50000L;
        Double commissionRate = 5.0;

        // When, Then
        assertThatThrownBy(() -> project.updateFundingInfo(goalAmount, invalidStartDateTime, invalidEndDate, commissionRate, user))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Project 객체 리워드 정보 업데이트 시 변경된 정보가 저장됩니다.")
    void testUpdateRewards_Success() {
        final Long userId = 1L;
        // Given
        Project project = generateDefaultProject(userId);
        User user = project.getUser();

        List<Reward> newRewards = generateSampleRewards();

        // When
        project.updateRewards(newRewards, user);

        // Then
        assertThat(project.getRewards().get(0).getProject()).isEqualTo(project);
        assertThat(project.getRewards()).hasSize(newRewards.size());
    }

    @Test
    @DisplayName("Project 객체 프로젝트 승인 후 상태가 PREPARING으로 변경됩니다.")
    void testSubmit_Success() {
        final Long userId = 1L;
        // Given
        Project project = generateFullParamsProject(userId); // 필수 정보가 입력된 상태의 Project
        User user = project.getUser();

        // When
        project.submit(user);

        // Then
        assertThat(project.getStatus()).isEqualTo(ProjectStatus.PREPARING);
    }

    @Test
    @DisplayName("Project 객체 프로젝트 승인 시 상태가 WRITING이 아닌 경우 예외가 발생합니다.")
    void testSubmit_WritingProjectException() {
        final Long userId = 1L;
        // Given
        Project project = generateFullParamsProject(userId);
        User user = project.getUser();

        // When
        ReflectionTestUtils.setField(project, "status", ProjectStatus.PREPARING);

        // Then
        assertThatThrownBy(() -> project.submit(user))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("Project 객체 프로젝트 승인 시 필수 정보가 입력되지 않은 경우 예외가 발생합니다.")
    void testSubmit_RequiredFieldsException() {
        final Long userId = 1L;
        // Given
        Project project = generateDefaultProject(userId); // 필수 정보가 입력되지 않은 상태의 Project
        User user = project.getUser();

        // When, Then
        assertThatThrownBy(() -> project.submit(user))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("Project 업데이트 시 사용자 id가 다른 경우 예외가 발생합니다.")
    void testUpdateUserValidationFailure() {
        final Long userId = 1L;
        final Long invalidUserId = 2L;
        // Given
        Project project = generateDefaultProject(userId);
        User invalidUser = generateUserWithId(invalidUserId);

        String newSummary = "Updated summary";
        ProjectCategory newCategory = ProjectCategory.BOARD_GAME;
        String newTitle = "Updated Title";
        List<ProjectImage> newProjectImages = generateSampleProjectImages();
        String newIntroduction = "Updated introduction";
        Long goalAmount = 50000L;
        LocalDateTime startDateTime = LocalDateTime.now().plusDays(1);
        LocalDate endDate = LocalDate.now().plusDays(30);
        Double commissionRate = 5.0;
        List<Reward> newRewards = generateSampleRewards();

        // When, Then
        assertThatThrownBy(() -> project.updateBasicInfo(newSummary, newCategory, newTitle, newProjectImages, invalidUser))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> project.updateIntroduction(newIntroduction, invalidUser))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> project.updateFundingInfo(goalAmount, startDateTime, endDate, commissionRate, invalidUser))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> project.updateRewards(newRewards, invalidUser))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> project.submit(invalidUser))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> project.deleteProject(invalidUser))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Project를 후원하면 후원자 수가 1 증가하고 현재 금액이 후원 금액만큼 증가합니다.")
    void testUpdateSponsorAddInfo_Success() {
        final Long userId = 1L;
        final Long sponsorAmount = 300L;
        // Given
        Project project = generateFullParamsProject(userId);
        ReflectionTestUtils.setField(project, "status", ProjectStatus.PROCEEDING);

        // When
        Long originalAmount = project.getCurrentAmount();
        Long originalSponsorCount = project.getSponsorCount();
        project.updateSponsorAddInfo(sponsorAmount);

        // Then
        assertThat(project.getCurrentAmount()).isEqualTo(originalAmount + sponsorAmount);
        assertThat(project.getSponsorCount()).isEqualTo(originalSponsorCount + 1);
    }

    @Test
    @DisplayName("Project 후원 시 후원 금액이 0 이하일 때 예외가 발생합니다.")
    void testUpdateSponsorAddInfo_NegativeSponsorAmountException() {
        final Long userId = 1L;
        final Long invalidSponsorAmount = -1L;
        // Given
        Project project = generateFullParamsProject(userId);
        ReflectionTestUtils.setField(project, "status", ProjectStatus.PROCEEDING);

        // When, Then
        assertThatThrownBy(() -> project.updateSponsorAddInfo(invalidSponsorAmount))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Project 후원 시 Project 상태가 PROCEEDING이 아닌 경우 예외가 발생합니다.")
    void testUpdateSponsorAddInfo_NotProceedingProjectException() {
        final Long userId = 1L;
        // Given
        Project project = generateFullParamsProject(userId);
        ReflectionTestUtils.setField(project, "status", ProjectStatus.PREPARING);

        // When, Then
        assertThatThrownBy(() -> project.updateSponsorAddInfo(1L))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("Project를 후원 취소하면 후원자 수가 1 감소하고 현재 금액이 후원 금액만큼 감소합니다.")
    void testUpdateSponsorCancelInfo_Success() {
        final Long userId = 1L;
        final Long sponsorAmount = 300L;
        // Given
        Project project = generateFullParamsProject(userId);
        ReflectionTestUtils.setField(project, "status", ProjectStatus.PROCEEDING);

        // When
        Long originalAmount = project.getCurrentAmount();
        Long originalSponsorCount = project.getSponsorCount();
        project.updateSponsorDeleteInfo(sponsorAmount);

        // Then
        assertThat(project.getCurrentAmount()).isEqualTo(originalAmount - sponsorAmount);
        assertThat(project.getSponsorCount()).isEqualTo(originalSponsorCount - 1);
    }

    @Test
    @DisplayName("Project 후원 취소 시 후원 금액이 0 이하일 때 예외가 발생합니다.")
    void testUpdateSponsorCancelInfo_NegativeSponsorAmountException() {
        final Long userId = 1L;
        final Long invalidSponsorAmount = -1L;
        // Given
        Project project = generateFullParamsProject(userId);
        ReflectionTestUtils.setField(project, "status", ProjectStatus.PROCEEDING);

        // When, Then
        assertThatThrownBy(() -> project.updateSponsorDeleteInfo(invalidSponsorAmount))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Project 삭제 시 프로젝트 상태가 CANCEL 상태로 변경되고 deletedAt이 저장됩니다.")
    void testDeleteProject_Success() {
        final Long userId = 1L;
        // Given
        Project project = generateFullParamsProject(userId);
        User user = project.getUser();

        // When
        project.deleteProject(user);

        // Then
        assertThat(project.getStatus()).isEqualTo(ProjectStatus.CANCEL);
        assertThat(project.getDeletedAt()).isNotNull();
    }

    @Test
    @DisplayName("Project 삭제시 진행 중이면서 후원자가 있는 경우 예외가 발생합니다.")
    void testDeleteProject_ProceedingProjectWithSponsorException() {
        final Long userId = 1L;
        // Given
        Project project = generateFullParamsProject(userId);
        User user = project.getUser();

        ReflectionTestUtils.setField(project, "status", ProjectStatus.PROCEEDING);
        ReflectionTestUtils.setField(project, "sponsorCount", 1L);

        // When, Then
        assertThatThrownBy(() -> project.deleteProject(user))
                .isInstanceOf(IllegalStateException.class);
    }

    @ParameterizedTest
    @MethodSource("provideInvalidProjectStatus")
    @DisplayName("Project 삭제시 펀딩 기간이 종료된 상태인 경우 예외가 발생합니다.")
    void testDeleteProject_FundingCompleteProjectException(ProjectStatus invalidProjectStatus) {
        final Long userId = 1L;
        // Given
        Project project = generateFullParamsProject(userId);
        User user = project.getUser();

        ReflectionTestUtils.setField(project, "status", invalidProjectStatus);

        // When, Then
        assertThatThrownBy(() -> project.deleteProject(user))
                .isInstanceOf(IllegalStateException.class);
    }
}
