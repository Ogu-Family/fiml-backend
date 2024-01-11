package kpl.fiml.notice.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Schema(description = "공지사항 삭제 응답 DTO")
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class NoticeDeleteResponse {
    @Schema(description = "삭제된 공지시항 id", example = "1")
    private final Long id;
    @Schema(description = "공지사항 작성자 id", example = "1")
    private final Long userId;
    @Schema(description = "삭제된 시간")
    private final LocalDateTime deletedAt;

    public static NoticeDeleteResponse of(Long id, Long userId, LocalDateTime deletedAt) {
        return new NoticeDeleteResponse(id, userId, deletedAt);
    }
}
