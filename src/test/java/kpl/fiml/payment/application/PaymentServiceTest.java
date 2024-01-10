package kpl.fiml.payment.application;

import kpl.fiml.payment.domain.Payment;
import kpl.fiml.payment.domain.PaymentRepository;
import kpl.fiml.payment.domain.PaymentStatus;
import kpl.fiml.payment.dto.response.PaymentDto;
import kpl.fiml.project.domain.Project;
import kpl.fiml.project.domain.Reward;
import kpl.fiml.project.domain.enums.ProjectCategory;
import kpl.fiml.sponsor.domain.Sponsor;
import kpl.fiml.sponsor.domain.SponsorRepository;
import kpl.fiml.user.application.UserService;
import kpl.fiml.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private SponsorRepository sponsorRepository;

    @InjectMocks
    private PaymentService paymentService;

    private User user;
    private Sponsor sponsor;
    private Payment payment1;
    private Payment payment2;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .email("abc@gmail.com")
                .contact("01012345678")
                .build();
        ReflectionTestUtils.setField(user, "id", 1L);

        Project project = Project.builder()
                .summary("summary")
                .category(ProjectCategory.ART)
                .user(user)
                .build();
        ReflectionTestUtils.setField(project, "endAt", LocalDateTime.now().plusDays(1L));

        Reward reward = Reward.builder()
                .quantityLimited(false)
                .maxPurchaseQuantity(100000)
                .price(10000L)
                .build();
        reward.setProject(project);

        sponsor = Sponsor.builder()
                .user(user)
                .reward(reward)
                .totalAmount(20000L)
                .build();

        payment1 = createPayment();
        payment2 = createPayment();
    }

    @Test
    @DisplayName("결제 정보를 조회할 수 있다.")
    void testGetPaymentsOfSuccessAndFail() {
        // given
        payment1.successPayed();
        payment2.failPayed();

        when(userService.getById(any())).thenReturn(user);
        when(sponsorRepository.findByIdAndDeletedAtIsNull(sponsor.getId())).thenReturn(Optional.of(sponsor));
        when(paymentRepository.findAllBySponsor(sponsor)).thenReturn(List.of(payment1, payment2));

        // when
        List<PaymentDto> dtos = paymentService.getPaymentsOfSuccessAndFail(sponsor.getId(), user.getId());

        // then
        assertThat(dtos).hasSize(2)
                .extracting(PaymentDto::getPaymentStatus)
                .contains(PaymentStatus.SUCCESS.getDisplayName(), PaymentStatus.FAIL.getDisplayName());
    }

    @Test
    @DisplayName("취소된 후원에 대한 결제 정보를 삭제할 수 있다.")
    void testDeletePayments() {
        // given
        when(paymentRepository.findAllBySponsor(sponsor)).thenReturn(List.of(payment1, payment2));

        // when
        paymentService.deletePayments(sponsor);

        // then
        assertThat(payment1.getDeletedAt()).isNotNull();
        assertThat(payment2.getDeletedAt()).isNotNull();
    }

    @Test
    @DisplayName("결제를 진행할 수 있다.")
    void testTryPay() {
        // given
        ReflectionTestUtils.setField(user, "cash", 30000L);

        when(paymentRepository.findAllByStatusAndDeletedAtIsNull(PaymentStatus.WAIT)).thenReturn(List.of(payment1, payment2));
        when(paymentRepository.countBySponsor(sponsor)).thenReturn(1L);

        // when
        paymentService.tryPay();

        // then
        assertThat(payment1.getStatus()).isEqualTo(PaymentStatus.SUCCESS);
        assertThat(payment2.getStatus()).isEqualTo(PaymentStatus.FAIL);
    }

    Payment createPayment() {
        return Payment.builder()
                .sponsor(sponsor)
                .requestedAt(LocalDateTime.now())
                .build();
    }
}
