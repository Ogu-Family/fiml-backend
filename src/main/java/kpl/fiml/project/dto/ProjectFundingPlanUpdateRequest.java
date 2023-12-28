package kpl.fiml.project.dto;

import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class ProjectFundingPlanUpdateRequest {

    private Long goalAmount;
    private LocalDateTime fundingStartDateTime;
    private LocalDate fundingEndDate; // 저장 시 시간 23:59:59로 고정
    // TODO: 0-100 실수 범위 검증 추가
    private Double commissionRate;
}
