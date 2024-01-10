package kpl.fiml.project.exception.project;

import kpl.fiml.global.exception.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ProjectGlobalExceptionHandler {

    @ExceptionHandler(ProjectFoundException.class)
    public ResponseEntity<ErrorResponse> catchProjectNotFoundException(ProjectFoundException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(e.getErrorCode(), e.getMessage()));
    }

    @ExceptionHandler(ProjectAccessException.class)
    public ResponseEntity<ErrorResponse> catchProjectAccessException(ProjectAccessException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(e.getErrorCode(), e.getMessage()));
    }

    @ExceptionHandler(ProjectDeleteException.class)
    public ResponseEntity<ErrorResponse> catchProjectDeleteException(ProjectDeleteException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(e.getErrorCode(), e.getMessage()));
    }

    @ExceptionHandler(ProjectFieldValueException.class)
    public ResponseEntity<ErrorResponse> catchProjectFieldValueException(ProjectFieldValueException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(e.getErrorCode(), e.getMessage()));
    }

    @ExceptionHandler(ProjectFundingException.class)
    public ResponseEntity<ErrorResponse> catchProjectFundingException(ProjectFundingException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(e.getErrorCode(), e.getMessage()));
    }

    @ExceptionHandler(ProjectLikeException.class)
    public ResponseEntity<ErrorResponse> catchProjectLikeException(ProjectLikeException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(e.getErrorCode(), e.getMessage()));
    }

    @ExceptionHandler(ProjectSubmitException.class)
    public ResponseEntity<ErrorResponse> catchProjectSubmitException(ProjectSubmitException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(e.getErrorCode(), e.getMessage()));
    }
}
