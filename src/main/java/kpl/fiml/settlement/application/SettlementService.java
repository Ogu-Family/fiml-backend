package kpl.fiml.settlement.application;

import kpl.fiml.project.domain.Project;
import kpl.fiml.project.domain.ProjectRepository;
import kpl.fiml.project.domain.enums.ProjectStatus;
import kpl.fiml.project.exception.project.ProjectErrorCode;
import kpl.fiml.project.exception.project.ProjectFoundException;
import kpl.fiml.settlement.domain.Settlement;
import kpl.fiml.settlement.domain.SettlementRepository;
import kpl.fiml.settlement.dto.response.SettlementDto;
import kpl.fiml.settlement.exception.SettlementAccessDeniedException;
import kpl.fiml.settlement.exception.SettlementErrorCode;
import kpl.fiml.settlement.exception.SettlementNotFoundException;
import kpl.fiml.user.application.UserService;
import kpl.fiml.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SettlementService {

    private static final int SETTLEMENT_OFFSET_DAY = 14;

    private final UserService userService;

    private final SettlementRepository settlementRepository;
    private final ProjectRepository projectRepository;

    @Scheduled(cron = "0 0 9 * * *", zone = "Asia/Seoul")
    @Transactional
    public void createSettlement() {
        List<Project> projects = projectRepository.findAllByStatus(ProjectStatus.FUNDING_COMPLETE)
                .stream()
                .filter(project -> project.getEndAt().plusDays(SETTLEMENT_OFFSET_DAY).isBefore(LocalDateTime.now()))
                .toList();

        for (Project project : projects) {
            Long settlementAmount = project.getCurrentAmount();
            project.getUser().increaseCash(settlementAmount);
            project.updateStatusToSettlementComplete();

            Settlement settlement = Settlement.builder()
                    .project(project)
                    .settleAmount(settlementAmount)
                    .settledAt(LocalDateTime.now())
                    .build();
            settlementRepository.save(settlement);
        }
    }

    public SettlementDto getSettlementByProject(Long projectId, Long userId) {
        User user = userService.getById(userId);
        Project project = projectRepository.findByIdWithUser(projectId)
                .orElseThrow(() -> new ProjectFoundException(ProjectErrorCode.PROJECT_NOT_FOUND));

        if (!user.isSameUser(project.getUser())) {
            throw new SettlementAccessDeniedException(SettlementErrorCode.SETTLEMENT_ACCESS_DENIED);
        }

        Settlement settlement = settlementRepository.findByProject(project)
                .orElseThrow(() -> new SettlementNotFoundException(SettlementErrorCode.SETTLEMENT_NOT_FOUND));

        return SettlementDto.of(settlement.getSettleAmount(), settlement.getSettledAt());
    }
}
