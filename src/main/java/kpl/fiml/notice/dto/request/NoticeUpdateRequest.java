package kpl.fiml.notice.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class NoticeUpdateRequest {

    @NotBlank(message = "공지사항 내용은 필수 입력 값 입니다.")
    private String content;
}
