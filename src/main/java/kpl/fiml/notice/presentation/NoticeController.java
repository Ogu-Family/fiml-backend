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
        //TODO : userId - authentication 처리
        NoticeCreateResponse response = noticeService.createNotice(request.getUserId(), request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/notices/{id}")
    public ResponseEntity<NoticeUpdateResponse> updateNotice(@PathVariable Long id, @RequestBody NoticeUpdateRequest request) {
        // TODO : userId - authentication 처리
        NoticeUpdateResponse response = noticeService.updateNotice(request.getUserId(), id, request);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/notices/{id}")
    public ResponseEntity<NoticeDto> findById(@PathVariable Long id) {
        NoticeDto dto = noticeService.findById(id);

        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

    @DeleteMapping("/notices/{id}")
    public ResponseEntity<NoticeDeleteResponse> deleteById(@PathVariable Long id, @RequestParam Long userId) {
        // TODO : userId - authentication 처리
        NoticeDeleteResponse response = noticeService.deleteById(id, userId);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
