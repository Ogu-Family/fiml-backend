package kpl.fiml.payment.presentation;

import kpl.fiml.payment.application.PaymentService;
import kpl.fiml.payment.dto.PaymentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping("/payments/{sponsorId}")
    public ResponseEntity<List<PaymentDto>> getPaymentsOfSuccessAndFail(@PathVariable Long sponsorId) {
        List<PaymentDto> responses = paymentService.getPaymentsOfSuccessAndFail(sponsorId);

        return ResponseEntity.ok(responses);
    }
}
