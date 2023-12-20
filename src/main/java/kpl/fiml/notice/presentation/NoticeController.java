package kpl.fiml.notice.presentation;

import kpl.fiml.notice.application.NoticeService;
import kpl.fiml.notice.dto.NoticeCreateRequest;
import kpl.fiml.notice.dto.NoticeCreateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class NoticeController {

    private final NoticeService noticeService;

    @PostMapping("/notices")
    public ResponseEntity<NoticeCreateResponse> createNotice(@RequestBody NoticeCreateRequest request) {
        NoticeCreateResponse response = noticeService.createNotice(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
