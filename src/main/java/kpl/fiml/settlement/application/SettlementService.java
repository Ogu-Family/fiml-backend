package kpl.fiml.settlement.application;

import kpl.fiml.project.domain.Project;
import kpl.fiml.project.domain.ProjectRepository;
import kpl.fiml.project.domain.enums.ProjectStatus;
import kpl.fiml.settlement.domain.Settlement;
import kpl.fiml.settlement.domain.SettlementRepository;
import kpl.fiml.settlement.dto.SettlementDto;
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
        Project project = projectRepository.findByIdAndDeletedAtIsNull(projectId)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 프로젝트가 존재하지 않습니다."));

        if (!user.isSameUser(project.getUser())) {
            throw new IllegalArgumentException("프로젝트 창작자만 정산 내역을 조회할 수 있습니다.");
        }

        Settlement settlement = settlementRepository.findByProject(project)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 정산 내역이 존재하지 않습니다."));

        return SettlementDto.of(settlement.getSettleAmount(), settlement.getSettledAt());
    }
}
