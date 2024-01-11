package kpl.fiml.comment.domain;

import kpl.fiml.TestDataFactory;
import kpl.fiml.comment.exception.CommentPermissionException;
import kpl.fiml.notice.domain.Notice;
import kpl.fiml.project.domain.Project;
import kpl.fiml.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CommentTest {

    @Test
    @DisplayName("댓글 수정 성공: 유효한 업데이트")
    void testUpdateComment_Success_ValidUpdate() {
        // Given
        User user = TestDataFactory.generateUserWithId(1L);
        Project project = TestDataFactory.generateDefaultProject(user.getId());
        Notice notice = TestDataFactory.generateNotice(user, project);
        Comment comment = TestDataFactory.generateComment(user, notice);

        // When
        comment.updateContent("updated content", user);

        // Then
        assertEquals("updated content", comment.getContent(), "댓글이 정상적으로 업데이트되었어야 합니다.");
    }

    @Test
    @DisplayName("댓글 내용 업데이트 실패: 로그인 사용자와 댓글 작성자가 다름")
    void testUpdateComment_Failure_DifferentUsers() {
        // Given
        Long commentId = 1L;
        String updatedContent = "Updated comment content";
        User loginUser = TestDataFactory.generateUserWithId(2L);
        User originalCommentUser = TestDataFactory.generateUserWithId(3L);

        Comment comment = TestDataFactory.generateComment(originalCommentUser,
                TestDataFactory.generateNotice(originalCommentUser, TestDataFactory.generateDefaultProject(originalCommentUser.getId())));
        ReflectionTestUtils.setField(comment, "id", commentId);

        // When/Then
        assertThrows(CommentPermissionException.class, () -> comment.updateContent(updatedContent, loginUser));
    }

    @Test
    @DisplayName("댓글 삭제 실패: 로그인 사용자와 댓글 작성자가 다름")
    void testDeleteComment_Failure_DifferentUsers() {
        // Given
        Long commentId = 1L;
        User loginUser = TestDataFactory.generateUserWithId(2L);
        User originalCommentUser = TestDataFactory.generateUserWithId(3L);

        Comment comment = TestDataFactory.generateComment(originalCommentUser,
                TestDataFactory.generateNotice(originalCommentUser, TestDataFactory.generateDefaultProject(originalCommentUser.getId())));
        ReflectionTestUtils.setField(comment, "id", commentId);

        // When/Then
        assertThrows(CommentPermissionException.class, () -> comment.deleteComment(loginUser));
    }


}