package kpl.fiml.payment.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kpl.fiml.payment.application.PaymentService;
import kpl.fiml.payment.dto.response.PaymentDto;
import kpl.fiml.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Payment Controller", description = "결제 컨트롤러")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class PaymentController {

    private final PaymentService paymentService;

    @Operation(summary = "결제 조회", description = "특정 후원에 대한 결제 내역 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "결제 내역 조회 성공"),
            @ApiResponse(responseCode = "400", description = "다른 회원의 후원 정보에 대한 결제 조회 시도"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 회원으로 요청됨"),
            @ApiResponse(responseCode = "403", description = "접근 권한이 없는 회원으로 요청됨"),
            @ApiResponse(responseCode = "404", description = "1. 존재하지 않는 회원 아이디로 요청됨\n 2. 존재하지 않는 후원 아이디로 요청됨")
    })
    @Parameters({
            @Parameter(name = "sponsorId", description = "후원 아이디")
    })
    @GetMapping("/payments/{sponsorId}")
    public ResponseEntity<List<PaymentDto>> getPaymentsOfSuccessAndFail(@PathVariable Long sponsorId, @AuthenticationPrincipal User user) {
        List<PaymentDto> responses = paymentService.getPaymentsOfSuccessAndFail(sponsorId, user.getId());

        return ResponseEntity.ok(responses);
    }
}
