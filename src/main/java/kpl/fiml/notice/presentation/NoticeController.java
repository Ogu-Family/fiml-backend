package kpl.fiml.notice.presentation;

import kpl.fiml.notice.application.NoticeService;
import kpl.fiml.notice.dto.*;
import kpl.fiml.notice.dto.request.NoticeCreateRequest;
import kpl.fiml.notice.dto.request.NoticeUpdateRequest;
import kpl.fiml.notice.dto.response.NoticeCreateResponse;
import kpl.fiml.notice.dto.response.NoticeDeleteResponse;
import kpl.fiml.notice.dto.response.NoticeUpdateResponse;
import kpl.fiml.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class NoticeController {

    private final NoticeService noticeService;

    @PostMapping("/notices")
    public ResponseEntity<NoticeCreateResponse> createNotice(@RequestBody NoticeCreateRequest request,
                                                             @AuthenticationPrincipal User user) {
        NoticeCreateResponse response = noticeService.createNotice(user.getId(), request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/notices/{id}")
    public ResponseEntity<NoticeUpdateResponse> updateNotice(@PathVariable Long id, @RequestBody NoticeUpdateRequest request,
                                                             @AuthenticationPrincipal User user) {
        NoticeUpdateResponse response = noticeService.updateNotice(user.getId(), id, request);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/notices/{id}")
    public ResponseEntity<NoticeDto> findById(@PathVariable Long id) {
        NoticeDto dto = noticeService.findById(id);

        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

    @GetMapping("/notices/user/{userId}")
    public ResponseEntity<List<NoticeDto>> findByUserId(@PathVariable Long userId) {
        List<NoticeDto> noticeDtoList = noticeService.findAllByUserId(userId);

        return ResponseEntity.status(HttpStatus.OK).body(noticeDtoList);
    }

    @GetMapping("/projects/{projectId}/notices")
    public ResponseEntity<List<NoticeDto>> findAllByProjectId(@PathVariable Long projectId) {
        List<NoticeDto> response = noticeService.findAllByProjectId(projectId);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/notices/{id}")
    public ResponseEntity<NoticeDeleteResponse> deleteById(@PathVariable Long id, @AuthenticationPrincipal User user) {
        NoticeDeleteResponse response = noticeService.deleteById(id, user.getId());

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
