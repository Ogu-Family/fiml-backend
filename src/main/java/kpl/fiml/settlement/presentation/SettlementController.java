package kpl.fiml.settlement.presentation;

import kpl.fiml.settlement.application.SettlementService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class SettlementController {

    private final SettlementService settlementService;
}
