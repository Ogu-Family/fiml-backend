package kpl.fiml.sponsor.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class SponsorDeleteResponse {

    @Schema(description = "취소한 후원 아이디")
    private final Long id;

    @Schema(description = "취소 일시")
    private final LocalDateTime deleteAt;

    public static SponsorDeleteResponse of(Long id, LocalDateTime deleteAt) {
        return new SponsorDeleteResponse(id, deleteAt);
    }
}
