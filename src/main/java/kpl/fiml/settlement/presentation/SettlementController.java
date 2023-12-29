package kpl.fiml.settlement.presentation;

import kpl.fiml.settlement.application.SettlementService;
import kpl.fiml.settlement.dto.SettlementDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<SettlementDto> getSettlement(@PathVariable Long projectId) {
        SettlementDto response = settlementService.getSettlement(projectId);

        return ResponseEntity.ok(response);
    }
}
