package kpl.fiml.notice.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Schema(description = "공지사항 생성 응당 DTO")
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class NoticeCreateResponse {
    @Schema(description = "공지사항 생성 id", example = "1")
    private final Long id;
    @Schema(description = "공지사항 작성자 id", example = "1")
    private final Long userId;
    @Schema(description = "공지사항 등록 프로젝트 id", example = "1")
    private final Long projectId;

    public static NoticeCreateResponse of(Long id, Long userId, Long projectId) {
        return new NoticeCreateResponse(id, userId, projectId);
    }
}
