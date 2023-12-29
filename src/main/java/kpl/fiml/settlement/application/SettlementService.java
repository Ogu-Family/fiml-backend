package kpl.fiml.settlement.application;

import kpl.fiml.project.domain.Project;
import kpl.fiml.project.domain.ProjectRepository;
import kpl.fiml.settlement.domain.Settlement;
import kpl.fiml.settlement.domain.SettlementRepository;
import kpl.fiml.settlement.dto.SettlementGetResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SettlementService {

    private final SettlementRepository settlementRepository;
    private final ProjectRepository projectRepository;

    public SettlementGetResponse getSettlement(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 프로젝트가 존재하지 않습니다."));

        Settlement settlement = settlementRepository.findByProject(project)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 정산 내역이 존재하지 않습니다."));

        return SettlementGetResponse.of(settlement.getSettleAmount(), settlement.getSettledAt());
    }
}
