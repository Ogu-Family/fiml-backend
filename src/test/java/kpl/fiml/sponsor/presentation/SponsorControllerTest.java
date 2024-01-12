package kpl.fiml.sponsor.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import kpl.fiml.customMockUser.WithCustomMockUser;
import kpl.fiml.project.domain.Project;
import kpl.fiml.project.domain.ProjectRepository;
import kpl.fiml.project.domain.Reward;
import kpl.fiml.project.domain.RewardRepository;
import kpl.fiml.project.domain.enums.ProjectCategory;
import kpl.fiml.project.domain.enums.ProjectStatus;
import kpl.fiml.sponsor.domain.Sponsor;
import kpl.fiml.sponsor.domain.SponsorRepository;
import kpl.fiml.sponsor.dto.request.SponsorCreateRequest;
import kpl.fiml.sponsor.dto.request.SponsorUpdateRequest;
import kpl.fiml.user.domain.User;
import kpl.fiml.user.domain.UserRepository;
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

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class SponsorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private RewardRepository rewardRepository;

    @Autowired
    private SponsorRepository sponsorRepository;

    @BeforeEach
    void setUp() {
        sponsorRepository.deleteAll();
        rewardRepository.deleteAll();
        projectRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @WithCustomMockUser
    @DisplayName("후원 정보를 생성할 수 있다.")
    void testCreateSponsor() throws Exception {
        // given
        User user = userRepository.save(createUser());
        Project project = projectRepository.save(createProject(user));
        Reward reward = rewardRepository.save(createReward(50000L, project));

        SponsorCreateRequest request = new SponsorCreateRequest();
        ReflectionTestUtils.setField(request, "rewardId", reward.getId());
        ReflectionTestUtils.setField(request, "totalAmount", reward.getPrice() + 10000L);

        // when-then
        mockMvc.perform(post("/api/v1/sponsors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.id").isNotEmpty());
    }

    @Test
    @WithCustomMockUser
    @DisplayName("특정 회원의 후원 리스트를 조회할 수 있다.")
    void testGetSponsorsByUser() throws Exception {
        // given
        User user = userRepository.save(createUser());
        Project project = projectRepository.save(createProject(user));
        Reward reward1 = rewardRepository.save(createReward(50000L, project));
        Reward reward2 = rewardRepository.save(createReward(12345L, project));
        Sponsor sponsor1 = sponsorRepository.save(createSponsor(user, reward1, reward1.getPrice()));
        Sponsor sponsor2 = sponsorRepository.save(createSponsor(user, reward2, reward2.getPrice() + 12346L));

        // when-then
        mockMvc.perform(get("/api/v1/sponsors"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].userId").value(sponsor1.getUser().getId()))
                .andExpect(jsonPath("$.[0].rewardId").value(sponsor1.getReward().getId()))
                .andExpect(jsonPath("$.[0].totalAmount").value(sponsor1.getTotalAmount()))
                .andExpect(jsonPath("$.[0].sponsorStatus").value(sponsor1.getStatus().getDisplayName()))
                .andExpect(jsonPath("$.[1].userId").value(sponsor2.getUser().getId()))
                .andExpect(jsonPath("$.[1].rewardId").value(sponsor2.getReward().getId()))
                .andExpect(jsonPath("$.[1].totalAmount").value(sponsor2.getTotalAmount()))
                .andExpect(jsonPath("$.[1].sponsorStatus").value(sponsor2.getStatus().getDisplayName()));
    }

    @Test
    @WithCustomMockUser
    @DisplayName("특정 프로젝트의 후원 리스트를 조회할 수 있다.")
    void testGetSponsorsByProject() throws Exception {
        // given
        User user = userRepository.save(createUser());
        Project project = projectRepository.save(createProject(user));
        Reward reward1 = rewardRepository.save(createReward(50000L, project));
        Reward reward2 = rewardRepository.save(createReward(12345L, project));
        Sponsor sponsor1 = sponsorRepository.save(createSponsor(user, reward1, reward1.getPrice()));
        Sponsor sponsor2 = sponsorRepository.save(createSponsor(user, reward2, reward2.getPrice() + 12346L));

        // when-then
        mockMvc.perform(get("/api/v1/sponsors/project/{projectId}", project.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].userId").value(sponsor1.getUser().getId()))
                .andExpect(jsonPath("$.[0].rewardId").value(sponsor1.getReward().getId()))
                .andExpect(jsonPath("$.[0].totalAmount").value(sponsor1.getTotalAmount()))
                .andExpect(jsonPath("$.[0].sponsorStatus").value(sponsor1.getStatus().getDisplayName()))
                .andExpect(jsonPath("$.[1].userId").value(sponsor2.getUser().getId()))
                .andExpect(jsonPath("$.[1].rewardId").value(sponsor2.getReward().getId()))
                .andExpect(jsonPath("$.[1].totalAmount").value(sponsor2.getTotalAmount()))
                .andExpect(jsonPath("$.[1].sponsorStatus").value(sponsor2.getStatus().getDisplayName()));
    }

    @Test
    @WithCustomMockUser
    @DisplayName("후원 정보를 수정할 수 있다.")
    void testUpdateSponsor() throws Exception {
        // given
        User user = userRepository.save(createUser());
        Project project = projectRepository.save(createProject(user));
        Reward reward = rewardRepository.save(createReward(50000L, project));
        Sponsor sponsor = sponsorRepository.save(createSponsor(user, reward, reward.getPrice()));

        SponsorUpdateRequest request = new SponsorUpdateRequest();
        ReflectionTestUtils.setField(request, "rewardId", sponsor.getReward().getId());
        ReflectionTestUtils.setField(request, "totalAmount", sponsor.getTotalAmount() + 12345L);

        // when-then
        mockMvc.perform(patch("/api/v1/sponsors/{sponsorId}", sponsor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.userId").value(sponsor.getUser().getId()))
                .andExpect(jsonPath("$.rewardId").value(request.getRewardId()))
                .andExpect(jsonPath("$.totalAmount").value(request.getTotalAmount()))
                .andExpect(jsonPath("$.sponsorStatus").value(sponsor.getStatus().getDisplayName()));
    }

    @Test
    @WithCustomMockUser
    @DisplayName("후원을 취소할 수 있다.")
    void testDeleteSponsor() throws Exception {
        // given
        User user = userRepository.save(createUser());
        Project project = projectRepository.save(createProject(user));
        Reward reward = rewardRepository.save(createReward(50000L, project));
        Sponsor sponsor = sponsorRepository.save(createSponsor(user, reward, reward.getPrice()));

        // when-then
        mockMvc.perform(delete("/api/v1/sponsors/{sponsorId}", sponsor.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(sponsor.getId()))
                .andExpect(jsonPath("$.deleteAt").isNotEmpty());
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

    Reward createReward(Long price, Project project) {
        Reward reward = Reward.builder()
                .title("Sample Reward")
                .content("Sample reward content")
                .sequence(123456)
                .price(price)
                .quantityLimited(false)
                .totalQuantity(123456789)
                .deliveryDate(LocalDate.now())
                .quantityLimited(false)
                .maxPurchaseQuantity(100000)
                .build();
        reward.setProject(project);

        return reward;
    }

    Sponsor createSponsor(User user, Reward reward, Long totalAmount) {
        return Sponsor.builder()
                .user(user)
                .reward(reward)
                .totalAmount(totalAmount)
                .build();
    }
}
