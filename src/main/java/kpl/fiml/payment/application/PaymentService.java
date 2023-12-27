package kpl.fiml.payment.application;

import kpl.fiml.payment.domain.Payment;
import kpl.fiml.payment.domain.PaymentRepository;
import kpl.fiml.payment.domain.PaymentStatus;
import kpl.fiml.sponsor.domain.Sponsor;
import kpl.fiml.sponsor.domain.SponsorStatus;
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

    private final PaymentRepository paymentRepository;

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
                sponsor.updateStatus(SponsorStatus.COMPLETE);
            } catch (IllegalArgumentException e) {
                payment.failPayed();

                if (paymentRepository.countBySponsor(sponsor) == MAX_PAYMENT_TRIAL) {
                    sponsor.updateStatus(SponsorStatus.PAYMENT_FAIL);
                } else {
                    paymentRepository.save(Payment.builder()
                            .sponsor(sponsor)
                            .requestedAt(LocalDateTime.of(LocalDate.now().plusDays(NEXT_PAYMENT_OFFSET), LocalTime.of(PAYMENT_PERIOD_HOUR, PAYMENT_PERIOD_MINUTE)))
                            .build());
                    sponsor.updateStatus(SponsorStatus.PAYMENT_PROCEEDING);
                }
            }

        }
    }

    private List<Payment> getRequiredPayments() {
        return paymentRepository.findAllByStatus(PaymentStatus.WAIT)
                .stream()
                .filter(payment ->
                        payment.getRequestedAt().isEqual(LocalDateTime.now()) || payment.getRequestedAt().isBefore(LocalDateTime.now())
                )
                .toList();
    }
}
