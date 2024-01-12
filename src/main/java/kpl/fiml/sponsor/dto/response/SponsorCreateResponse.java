package kpl.fiml.sponsor.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class SponsorCreateResponse {

    @Schema(description = "생성된 후원 아이디", example = "1")
    private final Long id;

    public static SponsorCreateResponse of(Long id) {
        return new SponsorCreateResponse(id);
    }
}
