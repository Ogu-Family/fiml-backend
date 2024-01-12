package kpl.fiml.payment.presentation;

import kpl.fiml.customMockUser.WithCustomMockUser;
import kpl.fiml.payment.domain.Payment;
import kpl.fiml.payment.domain.PaymentRepository;
import kpl.fiml.payment.domain.PaymentStatus;
import kpl.fiml.project.domain.Project;
import kpl.fiml.project.domain.ProjectRepository;
import kpl.fiml.project.domain.Reward;
import kpl.fiml.project.domain.RewardRepository;
import kpl.fiml.project.domain.enums.ProjectCategory;
import kpl.fiml.project.domain.enums.ProjectStatus;
import kpl.fiml.sponsor.domain.Sponsor;
import kpl.fiml.sponsor.domain.SponsorRepository;
import kpl.fiml.user.domain.User;
import kpl.fiml.user.domain.UserRepository;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class PaymentControllerTest {

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

    @Autowired
    private PaymentRepository paymentRepository;

    @Test
    @WithCustomMockUser
    @DisplayName("결제 정보를 조회할 수 있다.")
    void testGetPaymentsOfSuccessAndFail() throws Exception {
        // given
        User user = userRepository.save(createUser());
        Project project = projectRepository.save(createProject(user));
        Reward reward = rewardRepository.save(createReward(12435423L, project));
        Sponsor sponsor = sponsorRepository.save(createSponsor(user, reward, reward.getPrice() + 1242523L));
        Payment payment1 = paymentRepository.save(createPayment(sponsor, PaymentStatus.FAIL));
        Payment payment2 = paymentRepository.save(createPayment(sponsor, PaymentStatus.SUCCESS));

        // when-then
        mockMvc.perform(get("/api/v1/payments/{sponsorId}", sponsor.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].paymentStatus").value(payment1.getStatus().getDisplayName()))
                .andExpect(jsonPath("$.[1].paymentStatus").value(payment2.getStatus().getDisplayName()));
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

    Payment createPayment(Sponsor sponsor, PaymentStatus paymentStatus) {
        Payment payment = Payment.builder()
                .sponsor(sponsor)
                .requestedAt(LocalDateTime.now())
                .build();
        ReflectionTestUtils.setField(payment, "status", paymentStatus);

        return payment;
    }
}
