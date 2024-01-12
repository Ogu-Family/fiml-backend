package kpl.fiml.notice.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Schema(description = "공지사항 수정 DTO")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NoticeUpdateRequest {

    @Schema(description = "공지사항 내용", example = "notice update content")
    @NotBlank(message = "공지사항 내용은 필수 입력 값 입니다.")
    private String content;
}
