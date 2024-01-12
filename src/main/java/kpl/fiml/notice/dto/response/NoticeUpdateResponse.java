package kpl.fiml.notice.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Schema(description = "noitce 수정 응답 DTO")
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class NoticeUpdateResponse {
    @Schema(description = "공지사항 id", example = "1")
    private final Long id;
    @Schema(description = "공지사항 내용", example = "notice update content")
    private final String content;
    @Schema(description = "공지사항 작성자 id", example = "1")
    private final Long userId;

    public static NoticeUpdateResponse of(Long id, String content, Long userId) {
        return new NoticeUpdateResponse(id, content, userId);
    }
}
