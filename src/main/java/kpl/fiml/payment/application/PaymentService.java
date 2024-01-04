package kpl.fiml.payment.application;

import kpl.fiml.payment.domain.Payment;
import kpl.fiml.payment.domain.PaymentRepository;
import kpl.fiml.payment.domain.PaymentStatus;
import kpl.fiml.payment.dto.request.PaymentCreateRequest;
import kpl.fiml.payment.dto.response.PaymentDto;
import kpl.fiml.sponsor.application.SponsorService;
import kpl.fiml.sponsor.domain.Sponsor;
import kpl.fiml.user.application.UserService;
import kpl.fiml.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PaymentService {

    private static final int MAX_PAYMENT_TRIAL = 7;
    private static final int NEXT_PAYMENT_OFFSET = 1;
    private static final int PAYMENT_PERIOD_HOUR = 14;
    private static final int PAYMENT_PERIOD_MINUTE = 0;

    private final UserService userService;
    private final SponsorService sponsorService;

    private final PaymentRepository paymentRepository;

    public List<PaymentDto> getPaymentsOfSuccessAndFail(Long sponsorId, Long userId) {
        User user = userService.getById(userId);
        Sponsor sponsor = sponsorService.getSponsorById(sponsorId);

        if (!user.isSameUser(sponsor.getUser())) {
            throw new IllegalArgumentException("본인의 후원에 대한 결제 조회만 요청할 수 있습니다.");
        }

        List<PaymentDto> responses = paymentRepository.findAllBySponsor(sponsor)
                .stream()
                .filter(payment -> payment.getStatus().equals(PaymentStatus.SUCCESS) || payment.getStatus().equals(PaymentStatus.FAIL))
                .map(payment -> PaymentDto.of(payment.getStatus().getDisplayName(), payment.getRequestedAt(), payment.getApprovedAt()))
                .toList();

        return responses;
    }

    @Transactional
    public void createPayment(PaymentCreateRequest request) {
        paymentRepository.save(request.toEntity(LocalDateTime.of(request.getRequestedDay().plusDays(NEXT_PAYMENT_OFFSET), LocalTime.of(PAYMENT_PERIOD_HOUR, PAYMENT_PERIOD_MINUTE))));
    }

    @Transactional
    public void deletePayments(Sponsor sponsor) {
        List<Payment> payments = paymentRepository.findAllBySponsor(sponsor);

        for (Payment payment : payments) {
            payment.deletePayment();
        }
    }

    @Scheduled(cron = "0 0 14 * * *", zone = "Asia/Seoul")
    @Transactional
    public void tryPay() {
        List<Payment> payments = getRequiredPayments();

        for (Payment payment : payments) {
            Sponsor sponsor = payment.getSponsor();

            try {
                User user = sponsor.getUser();
                Long requiredPaymentAmount = sponsor.getTotalAmount();
                user.decreaseCash(requiredPaymentAmount);

                payment.successPayed();
                sponsor.updateStatusToComplete();
            } catch (IllegalArgumentException e) {
                payment.failPayed();

                if (paymentRepository.countBySponsor(sponsor) == MAX_PAYMENT_TRIAL) {
                    sponsor.paymentFail();
                } else {
                    createPayment(PaymentCreateRequest.of(sponsor, LocalDate.now()));
                    sponsor.updateStatusToPaymentProceeding();
                }
            }

        }
    }

    private List<Payment> getRequiredPayments() {
        return paymentRepository.findAllByStatusAndDeletedAtIsNull(PaymentStatus.WAIT)
                .stream()
                .filter(payment ->
                        payment.getRequestedAt().isEqual(LocalDateTime.now()) || payment.getRequestedAt().isBefore(LocalDateTime.now())
                )
                .toList();
    }
}
