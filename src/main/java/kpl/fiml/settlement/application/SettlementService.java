package kpl.fiml.settlement.application;

import kpl.fiml.settlement.domain.SettlementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SettlementService {

    private final SettlementRepository settlementRepository;
}
