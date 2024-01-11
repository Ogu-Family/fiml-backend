package kpl.fiml.settlement.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kpl.fiml.settlement.application.SettlementService;
import kpl.fiml.settlement.dto.response.SettlementDto;
import kpl.fiml.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Settlement Controller", description = "정산 컨트롤러")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class SettlementController {

    private final SettlementService settlementService;

    @Operation(summary = "정산 조회", description = "특정 프로젝트에 대한 정산 내역 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "정산 내역 조회 성공"),
            @ApiResponse(responseCode = "400", description = "다른 창작자의 프로젝트에 대한 결제 조회 시도"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 회원으로 요청됨"),
            @ApiResponse(responseCode = "403", description = "접근 권한이 없는 회원으로 요청됨"),
            @ApiResponse(responseCode = "404", description = "1. 존재하지 않는 회원 아이디로 요청됨\n 2. 존재하지 않는 프로젝트 아이디로 요청됨")
    })
    @Parameters({
            @Parameter(name = "projectId", description = "프로젝트 아이디")
    })
    @GetMapping("/settlements/{projectId}")
    public ResponseEntity<SettlementDto> getSettlementByProject(@PathVariable Long projectId, @AuthenticationPrincipal User user) {
        SettlementDto response = settlementService.getSettlementByProject(projectId, user.getId());

        return ResponseEntity.ok(response);
    }
}
