package kpl.fiml.comment.presentation;

import jakarta.validation.Valid;
import kpl.fiml.comment.application.CommentService;
import kpl.fiml.comment.dto.*;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/notice/{noticeId}/comments")
    ResponseEntity<CommentCreateResponse> create(@PathVariable Long noticeId, @Valid @RequestBody CommentCreateRequest request,
                                                 @AuthenticationPrincipal User user) {
        CommentCreateResponse response = commentService.create(user.getId(), noticeId, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/notice/{noticeId}/comments")
    ResponseEntity<List<CommentDto>> findAllByNoticeId(@PathVariable Long noticeId) {
        List<CommentDto> response = commentService.findAllByNoticeId(noticeId);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping("/comments/{id}")
    ResponseEntity<CommentUpdateResponse> update(@PathVariable Long id, @Valid @RequestBody CommentUpdateRequest request,
                                                 @AuthenticationPrincipal User user) {
        CommentUpdateResponse response = commentService.update(id, user.getId(), request);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/comments/{id}")
    ResponseEntity<CommentDeleteResponse> deleteById(@PathVariable Long id, @AuthenticationPrincipal User user) {
        CommentDeleteResponse response = commentService.deleteById(id, user.getId());

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
