package kpl.fiml.comment.presentation;

import kpl.fiml.comment.application.CommentService;
import kpl.fiml.comment.dto.CommentCreateRequest;
import kpl.fiml.comment.dto.CommentCreateResponse;
import kpl.fiml.comment.dto.CommentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/notice/{noticeId}/comments")
    ResponseEntity<CommentCreateResponse> create(@PathVariable Long noticeId, @RequestBody CommentCreateRequest request) {
        CommentCreateResponse response = commentService.create(noticeId, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/notice/{noticeId}/comments")
    ResponseEntity<List<CommentDto>> findAllByNoticeId(@PathVariable Long noticeId) {
        List<CommentDto> response = commentService.findAllByNoticeId(noticeId);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
