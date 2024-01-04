package kpl.fiml.sponsor.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class SponsorDeleteResponse {

    private final Long id;
    private final LocalDateTime deleteAt;

    public static SponsorDeleteResponse of(Long id, LocalDateTime deleteAt) {
        return new SponsorDeleteResponse(id, deleteAt);
    }
}
