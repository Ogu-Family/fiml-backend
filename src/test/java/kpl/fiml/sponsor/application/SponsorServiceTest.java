package kpl.fiml.sponsor.application;

import kpl.fiml.payment.application.PaymentService;
import kpl.fiml.project.application.ProjectService;
import kpl.fiml.project.domain.Project;
import kpl.fiml.project.domain.Reward;
import kpl.fiml.project.domain.RewardRepository;
import kpl.fiml.project.domain.enums.ProjectCategory;
import kpl.fiml.sponsor.domain.Sponsor;
import kpl.fiml.sponsor.domain.SponsorRepository;
import kpl.fiml.sponsor.dto.request.SponsorCreateRequest;
import kpl.fiml.sponsor.dto.request.SponsorUpdateRequest;
import kpl.fiml.sponsor.dto.response.SponsorCreateResponse;
import kpl.fiml.sponsor.dto.response.SponsorDeleteResponse;
import kpl.fiml.sponsor.dto.response.SponsorDto;
import kpl.fiml.user.application.UserService;
import kpl.fiml.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SponsorServiceTest {

    @Mock
    private SponsorRepository sponsorRepository;

    @Mock
    private RewardRepository rewardRepository;

    @Mock
    private UserService userService;

    @Mock
    private ProjectService projectService;

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private SponsorService sponsorService;

    private User user;

    @BeforeEach
    void setUp() throws Exception {
        user = User.builder()
                .email("abc@gmail.com")
                .contact("01012345678")
                .build();
        Field userId = user.getClass().getDeclaredField("id");
        userId.setAccessible(true);
        userId.set(user, 1L);
    }

    @Test
    @DisplayName("Sponsor를 생성할 수 있다.")
    void testCreateSponsor() throws Exception {
        // given
        SponsorCreateRequest request = new SponsorCreateRequest();
        Field rewardId = request.getClass().getDeclaredField("rewardId");
        rewardId.setAccessible(true);
        rewardId.set(request, 1L);
        Field totalAmount = request.getClass().getDeclaredField("totalAmount");
        totalAmount.setAccessible(true);
        totalAmount.set(request, 60000L);

        when(userService.getById(any())).thenReturn(user);

        Project project = createProject(user);
        Reward reward = createReward(50000L, project);
        when(rewardRepository.findByIdAndDeletedAtIsNull(any())).thenReturn(Optional.of(reward));

        when(sponsorRepository.save(any())).thenReturn(request.toEntity(user, reward));

        // when
        SponsorCreateResponse response = sponsorService.createSponsor(request, user.getId());

        // then
        assertThat(response).isNotNull();
        verify(sponsorRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("특정 회원의 후원 리스트를 조회할 수 있다.")
    void testGetSponsorsByUser() throws Exception {
        // given
        when(userService.getById(any())).thenReturn(user);

        Project project = createProject(user);
        Reward reward1 = createReward(50000L, project);
        Reward reward2 = createReward(123456L, project);

        Sponsor sponsor1 = createSponsor(reward1, 50000L);
        Sponsor sponsor2 = createSponsor(reward2, 987654L);
        when(sponsorRepository.findAllByUserAndDeletedAtIsNull(user)).thenReturn(List.of(sponsor1, sponsor2));

        // when
        List<SponsorDto> dtos = sponsorService.getSponsorsByUser(user.getId());

        // then
        assertThat(dtos).hasSize(2)
                .extracting(SponsorDto::getTotalAmount)
                .contains(sponsor1.getTotalAmount(), sponsor2.getTotalAmount());
    }

    @Test
    @DisplayName("특정 프로젝트에 대한 후원 리스트를 조회할 수 있다.")
    void testGetSponsorsByProject() throws Exception {
        // given
        when(userService.getById(any())).thenReturn(user);

        Project project = createProject(user);
        when(projectService.getProjectByIdWithUser(any())).thenReturn(project);

        Reward reward1 = createReward(50000L, project);
        Reward reward2 = createReward(123456L, project);
        when(rewardRepository.findAllByProjectAndDeletedAtIsNull(project)).thenReturn(List.of(reward1, reward2));

        Sponsor sponsor1 = createSponsor(reward1, 50000L);
        Sponsor sponsor2 = createSponsor(reward2, 987654L);
        Sponsor sponsor3 = createSponsor(reward2, 123499L);
        when(sponsorRepository.findAllByReward(reward1)).thenReturn(List.of(sponsor1));
        when(sponsorRepository.findAllByReward(reward2)).thenReturn(List.of(sponsor2, sponsor3));

        // when
        List<SponsorDto> dtos = sponsorService.getSponsorsByProject(1L, user.getId());

        // then
        assertThat(dtos).hasSize(3)
                .extracting(SponsorDto::getTotalAmount)
                .contains(sponsor1.getTotalAmount(), sponsor2.getTotalAmount(), sponsor3.getTotalAmount());
    }

    @Test
    @DisplayName("후원자가 후원 정보를 수정할 수 있다.")
    void testUpdateSponsor() throws Exception {
        // given
        SponsorUpdateRequest request = new SponsorUpdateRequest();
        Field rewardId = request.getClass().getDeclaredField("rewardId");
        rewardId.setAccessible(true);
        rewardId.set(request, 1L);
        Field totalAmount = request.getClass().getDeclaredField("totalAmount");
        totalAmount.setAccessible(true);
        totalAmount.set(request, 60000L);

        when(userService.getById(any())).thenReturn(user);

        Project project = createProject(user);
        Reward reward = createReward(50000L, project);
        when(rewardRepository.findByIdAndDeletedAtIsNull(any())).thenReturn(Optional.of(reward));

        Sponsor sponsor = createSponsor(reward, 50000L);
        when(sponsorRepository.findByIdAndDeletedAtIsNull(any())).thenReturn(Optional.of(sponsor));

        // when
        SponsorDto dto = sponsorService.updateSponsor(1L, request, user.getId());

        // then
        assertThat(dto.getTotalAmount()).isEqualTo(60000L);
    }

    @Test
    @DisplayName("후원자가 후원을 취소할 수 있다.")
    void testDeleteSponsorByUser() throws Exception {
        // given
        when(userService.getById(any())).thenReturn(user);

        Project project = createProject(user);
        Reward reward = createReward(50000L, project);
        Sponsor sponsor = createSponsor(reward, 50000L);
        when(sponsorRepository.findByIdAndDeletedAtIsNull(any())).thenReturn(Optional.of(sponsor));

        // when
        SponsorDeleteResponse response = sponsorService.deleteSponsorByUser(1L, user.getId());

        // then
        assertThat(response.getDeleteAt()).isNotNull();
    }

    @Test
    @DisplayName("특정 프로젝트에 대한 전체 후원을 취소할 수 있다.")
    void testDeleteSponsorsByProject() throws Exception {
        // given
        Project project = createProject(user);
        Reward reward1 = createReward(50000L, project);
        Reward reward2 = createReward(123456L, project);
        when(rewardRepository.findAllByProjectAndDeletedAtIsNull(project)).thenReturn(List.of(reward1, reward2));

        Sponsor sponsor1 = createSponsor(reward1, 50000L);
        Sponsor sponsor2 = createSponsor(reward2, 987654L);
        Sponsor sponsor3 = createSponsor(reward2, 123499L);
        when(sponsorRepository.findAllByReward(reward1)).thenReturn(List.of(sponsor1));
        when(sponsorRepository.findAllByReward(reward2)).thenReturn(List.of(sponsor2, sponsor3));

        // when
        sponsorService.deleteSponsorsByProject(project);

        // then
        assertThat(sponsor1.getDeletedAt()).isNotNull();
        assertThat(sponsor2.getDeletedAt()).isNotNull();
        assertThat(sponsor3.getDeletedAt()).isNotNull();
    }

    Project createProject(User user) throws Exception {
        Project project = Project.builder()
                .summary("summary")
                .category(ProjectCategory.ART)
                .user(user)
                .build();
        Field endAt = project.getClass().getDeclaredField("endAt");
        endAt.setAccessible(true);
        endAt.set(project, LocalDateTime.now().plusDays(1L));

        return project;
    }

    Reward createReward(Long price, Project project) {
        Reward reward = Reward.builder()
                .quantityLimited(false)
                .maxPurchaseQuantity(100000)
                .price(price)
                .build();
        reward.setProject(project);

        return reward;
    }

    Sponsor createSponsor(Reward reward, Long totalAmount) {
        return Sponsor.builder()
                .user(user)
                .reward(reward)
                .totalAmount(totalAmount)
                .build();
    }
}
