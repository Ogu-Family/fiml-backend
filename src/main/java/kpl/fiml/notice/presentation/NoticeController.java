package kpl.fiml.notice.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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

@Tag(name = "Notice Controller", description = "공지사항 컨트롤러")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class NoticeController {

    private final NoticeService noticeService;

    @Operation(summary = "공지사항 생성", description = "특정 프로젝트에 대한 공지사항 작성")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "공지사항 작성 성공"),
            @ApiResponse(responseCode = "403", description = "접근 권한이 없는 회원으로 요청됨"),
            @ApiResponse(responseCode = "404", description = "1. 존재하지 않는 회원 아이디로 요청됨\n 2. 존재하지 않는 프로젝트 아이디로 요청됨")
    })
    @PostMapping("/notices")
    public ResponseEntity<NoticeCreateResponse> createNotice(@Valid @RequestBody NoticeCreateRequest request,
                                                             @AuthenticationPrincipal User user) {
        NoticeCreateResponse response = noticeService.createNotice(user.getId(), request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/notices/{id}")
    public ResponseEntity<NoticeUpdateResponse> updateNotice(@PathVariable Long id, @Valid @RequestBody NoticeUpdateRequest request,
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
