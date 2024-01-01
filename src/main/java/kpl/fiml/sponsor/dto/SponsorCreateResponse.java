package kpl.fiml.sponsor.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class SponsorCreateResponse {

    private final Long id;

    public static SponsorCreateResponse of(Long id) {
        return new SponsorCreateResponse(id);
    }
}
