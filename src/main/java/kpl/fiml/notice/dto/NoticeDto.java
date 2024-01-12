package kpl.fiml.notice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Schema(description = "공지사항 상세정보 DTO")
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class NoticeDto {
    @Schema(description = "공지사항 id", example = "1")
    private final Long id;
    @Schema(description = "공자사항 내용", example = "notice content")
    private final String content;
    @Schema(description = "공지사항 생성일")
    private final LocalDateTime createdAt;
    @Schema(description = "공지사항 수정일")
    private final LocalDateTime updatedAt;

    public static NoticeDto of(Long id, String content, LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new NoticeDto(id, content, createdAt, updatedAt);
    }
}
