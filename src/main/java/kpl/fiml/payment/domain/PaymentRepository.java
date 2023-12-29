package kpl.fiml.payment.domain;

import kpl.fiml.sponsor.domain.Sponsor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findAllByStatus(PaymentStatus status);
    List<Payment> findAllBySponsor(Sponsor sponsor);
    Long countBySponsor(Sponsor sponsor);
}
