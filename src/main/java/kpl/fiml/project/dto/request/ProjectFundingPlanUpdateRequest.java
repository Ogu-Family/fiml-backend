package kpl.fiml.project.dto.request;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
public class ProjectFundingPlanUpdateRequest {

    private Long goalAmount;
    private LocalDateTime fundingStartDateTime;
    private LocalDate fundingEndDate; // 저장 시 시간 23:59:59로 고정
    private Double commissionRate;
}
