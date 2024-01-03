package kpl.fiml.settlement.presentation;

import kpl.fiml.settlement.application.SettlementService;
import kpl.fiml.settlement.dto.SettlementDto;
import kpl.fiml.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class SettlementController {

    private final SettlementService settlementService;

    @GetMapping("/settlements/{projectId}")
    public ResponseEntity<SettlementDto> getSettlementByProject(@PathVariable Long projectId, @AuthenticationPrincipal User user) {
        SettlementDto response = settlementService.getSettlementByProject(projectId, user.getId());

        return ResponseEntity.ok(response);
    }
}
