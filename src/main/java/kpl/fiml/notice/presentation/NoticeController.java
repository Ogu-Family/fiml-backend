package kpl.fiml.notice.presentation;

import kpl.fiml.notice.application.NoticeService;
import kpl.fiml.notice.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PatchMapping("/notices/{id}")
    public ResponseEntity<NoticeUpdateResponse> updateNotice(@PathVariable Long id, @RequestBody NoticeUpdateRequest request) {
        NoticeUpdateResponse response = noticeService.updateNotice(id, request);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/notices/{id}")
    public ResponseEntity<NoticeDto> findById(@PathVariable Long id) {
        NoticeDto dto = noticeService.findById(id);

        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

    @DeleteMapping("/notices/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
        Long deletedNoticeId = noticeService.deleteById(id);

        return ResponseEntity.status(HttpStatus.OK).body("Deleted Notice Id : " + deletedNoticeId);
    }
}
