package kpl.fiml.sponsor.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class SponsorDto {

    @Schema(description = "후원자 아이디")
    private final Long userId;

    @Schema(description = "후원한 리워드 아이디")
    private final Long rewardId;

    @Schema(description = "총 후원금", example = "40000L")
    private final Long totalAmount;

    @Schema(description = "후원 진행 상태", example = "펀딩 진행 중")
    private final String sponsorStatus;

    public static SponsorDto of(Long userId, Long rewardId, Long totalAmount, String sponsorStatus) {
        return new SponsorDto(userId, rewardId, totalAmount, sponsorStatus);
    }
}
