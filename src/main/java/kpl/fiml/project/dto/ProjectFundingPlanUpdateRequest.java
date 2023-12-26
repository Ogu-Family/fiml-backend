package kpl.fiml.project.dto;

import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class ProjectFundingPlanUpdateRequest {

    private Long goalAmount;
    private LocalDateTime fundingStartAt;
    private LocalDate fundingEndAt; // 시간은 23:59:59로 고정
    private Double commissionRate;
}
