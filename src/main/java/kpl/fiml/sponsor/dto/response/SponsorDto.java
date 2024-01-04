package kpl.fiml.sponsor.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class SponsorDto {

    private final Long userId;
    private final Long rewardId;
    private final Long totalAmount;
    private final String sponsorStatus;

    public static SponsorDto of(Long userId, Long rewardId, Long totalAmount, String sponsorStatus) {
        return new SponsorDto(userId, rewardId, totalAmount, sponsorStatus);
    }
}
