package kpl.fiml.comment.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kpl.fiml.comment.application.CommentService;
import kpl.fiml.comment.dto.CommentDto;
import kpl.fiml.comment.dto.request.CommentCreateRequest;
import kpl.fiml.comment.dto.request.CommentUpdateRequest;
import kpl.fiml.comment.dto.response.CommentCreateResponse;
import kpl.fiml.comment.dto.response.CommentDeleteResponse;
import kpl.fiml.comment.dto.response.CommentUpdateResponse;
import kpl.fiml.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Comment Controller", description = "댓글 컨트롤러")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "댓글 생성", description = "공지사항 댓글 작성")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "공지사항 댓글 작성 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 공지사항 아이디로 요청됨")
    })
    @Parameters({
            @Parameter(name = "noticeId", description = "공지사항 id")
    })
    @PostMapping("/notice/{noticeId}/comments")
    ResponseEntity<CommentCreateResponse> create(@PathVariable Long noticeId, @Valid @RequestBody CommentCreateRequest request,
                                                 @AuthenticationPrincipal User user) {
        CommentCreateResponse response = commentService.create(user.getId(), noticeId, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "공지사항 댓글 리스트 조회", description = "특정 공지사항에 대한 댓글 리스트를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "댓글 조회 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 공지사항 아이디로 요청됨")
    })
    @Parameters({
            @Parameter(name = "id", description = "공지사항 id")
    })
    @GetMapping("/notice/{noticeId}/comments")
    ResponseEntity<List<CommentDto>> findAllByNoticeId(@PathVariable Long noticeId) {
        List<CommentDto> response = commentService.findAllByNoticeId(noticeId);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "댓글 수정", description = "댓글을 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "댓글 수정 성공"),
            @ApiResponse(responseCode = "403", description = "접근 권한이 없는 회원으로 요청됨"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 댓글 아이디로 요청됨")
    })
    @Parameters({
            @Parameter(name = "id", description = "댓글 id")
    })
    @PatchMapping("/comments/{id}")
    ResponseEntity<CommentUpdateResponse> update(@PathVariable Long id, @Valid @RequestBody CommentUpdateRequest request,
                                                 @AuthenticationPrincipal User user) {
        CommentUpdateResponse response = commentService.update(id, user.getId(), request);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "댓글 삭제", description = "댓글을 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "댓글 삭제 성공"),
            @ApiResponse(responseCode = "403", description = "접근 권한이 없는 회원으로 요청됨"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 댓글 아이디로 요청됨")
    })
    @Parameters({
            @Parameter(name = "id", description = "댓글 id")
    })
    @DeleteMapping("/comments/{id}")
    ResponseEntity<CommentDeleteResponse> deleteById(@PathVariable Long id, @AuthenticationPrincipal User user) {
        CommentDeleteResponse response = commentService.deleteById(id, user.getId());

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
