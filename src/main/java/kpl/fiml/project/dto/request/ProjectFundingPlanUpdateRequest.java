package kpl.fiml.project.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description = "프로젝트 펀딩 계획 수정 정보")
@Getter
@Builder
public class ProjectFundingPlanUpdateRequest {

    @Schema(description = "프로젝트 목표 금액", example = "1000000")
    private Long goalAmount;

    @Schema(description = "프로젝트 펀딩 시작 날짜", example = "2059-01-01 00:00:00")
    private LocalDateTime fundingStartDateTime;

    @Schema(description = "프로젝트 펀딩 종료 날짜", example = "2059-12-31")
    private LocalDate fundingEndDate; // 저장 시 시간 23:59:59로 고정

    @Schema(description = "프로젝트 펀딩 수수료", example = "5.9")
    private Double commissionRate;
}
