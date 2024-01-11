package kpl.fiml.settlement.application;

import kpl.fiml.project.domain.Project;
import kpl.fiml.project.domain.ProjectRepository;
import kpl.fiml.project.domain.enums.ProjectCategory;
import kpl.fiml.project.domain.enums.ProjectStatus;
import kpl.fiml.settlement.domain.Settlement;
import kpl.fiml.settlement.domain.SettlementRepository;
import kpl.fiml.settlement.dto.response.SettlementDto;
import kpl.fiml.user.application.UserService;
import kpl.fiml.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SettlementServiceTest {

    private static final int SETTLEMENT_OFFSET_DAY = 14;

    @Mock
    private UserService userService;

    @Mock
    private SettlementRepository settlementRepository;

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private SettlementService settlementService;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .email("abc@gmail.com")
                .contact("01012345678")
                .build();
        ReflectionTestUtils.setField(user, "id", 1L);
    }

    @Test
    @DisplayName("정산 정보를 생성할 수 있다.")
    void testCreateSettlement() {
        // given
        Project project1 = createProject(100000L);
        Project project2 = createProject(123456L);
        Project project3 = createProject(9385734098L);

        when(projectRepository.findAllByStatus(ProjectStatus.FUNDING_COMPLETE)).thenReturn(List.of(project1, project2, project3));
        when(settlementRepository.save(any())).thenReturn(Settlement.builder().build());

        // when
        settlementService.createSettlement();

        // then
        verify(settlementRepository, times(3)).save(any());
        assertThat(project1.getStatus()).isEqualTo(ProjectStatus.SETTLEMENT_COMPLETE);
        assertThat(project2.getStatus()).isEqualTo(ProjectStatus.SETTLEMENT_COMPLETE);
        assertThat(project3.getStatus()).isEqualTo(ProjectStatus.SETTLEMENT_COMPLETE);
        assertThat(user.getCash()).isEqualTo(project1.getCurrentAmount() + project2.getCurrentAmount() + project3.getCurrentAmount());
    }

    @Test
    @DisplayName("특정 프로젝트에 대한 정산 정보를 조회할 수 있다.")
    void testGetSettlementByProject() {
        // given
        Project project = createProject(100000L);
        ReflectionTestUtils.setField(project, "id", 1L);

        Settlement settlement = Settlement.builder()
                .project(project)
                .settleAmount(project.getCurrentAmount())
                .settledAt(LocalDateTime.now())
                .build();

        when(userService.getById(user.getId())).thenReturn(user);
        when(projectRepository.findByIdWithUser(project.getId())).thenReturn(Optional.of(project));
        when(settlementRepository.findByProject(project)).thenReturn(Optional.of(settlement));

        // when
        SettlementDto dto = settlementService.getSettlementByProject(project.getId(), user.getId());

        // then
        assertThat(dto.getSettleAmount()).isEqualTo(project.getCurrentAmount());
        assertThat(dto.getSettledAt()).isNotNull();
    }

    Project createProject(Long currentAmount) {
        Project project = Project.builder()
                .summary("summary")
                .category(ProjectCategory.ART)
                .user(user)
                .build();
        ReflectionTestUtils.setField(project, "currentAmount", currentAmount);
        ReflectionTestUtils.setField(project, "status", ProjectStatus.FUNDING_COMPLETE);
        ReflectionTestUtils.setField(project, "endAt", LocalDateTime.now().minusDays(SETTLEMENT_OFFSET_DAY));

        return project;
    }
}
