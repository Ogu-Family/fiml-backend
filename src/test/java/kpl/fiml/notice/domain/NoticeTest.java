package kpl.fiml.notice.domain;

import kpl.fiml.TestDataFactory;
import kpl.fiml.notice.exception.NoticePermissionException;
import kpl.fiml.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class NoticeTest {

    @Test
    @DisplayName("공지사항 내용 업데이트 성공: 유효한 업데이트")
    void testUpdateNotice_Success_ValidUpdate() {
        // Given
        Long noticeId = 1L;
        String updatedContent = "Updated notice content";
        User loginUser = TestDataFactory.generateUserWithId(2L);

        Notice notice = TestDataFactory.generateNotice(loginUser,
                TestDataFactory.generateDefaultProject(loginUser.getId()));
        ReflectionTestUtils.setField(notice, "id", noticeId);

        // When
        notice.updateContent(updatedContent, loginUser);

        // Then
        assertEquals(updatedContent, notice.getContent());
    }

    @Test
    @DisplayName("공지사항 내용 업데이트 실패: 로그인 사용자와 공지사항 작성자가 다름")
    void testUpdateNotice_Fail_DifferentUsers() {
        // Given
        Long noticeId = 1L;
        String updatedContent = "Updated notice content";
        User loginUser = TestDataFactory.generateUserWithId(2L);
        User originalNoticeUser = TestDataFactory.generateUserWithId(3L);

        Notice notice = TestDataFactory.generateNotice(originalNoticeUser,
                TestDataFactory.generateDefaultProject(originalNoticeUser.getId()));
        ReflectionTestUtils.setField(notice, "id", noticeId);

        // When/Then
        assertThrows(NoticePermissionException.class, () -> notice.updateContent(updatedContent, loginUser));
    }

    @Test
    @DisplayName("공지사항 삭제 성공")
    void testDeleteNotice_Success() {
        // Given
        Long noticeId = 1L;
        User loginUser = TestDataFactory.generateUserWithId(2L);

        Notice notice = TestDataFactory.generateNotice(loginUser,
                TestDataFactory.generateDefaultProject(loginUser.getId()));
        ReflectionTestUtils.setField(notice, "id", noticeId);

        // When
        notice.deleteNotice(loginUser);

        // Then
        assertTrue(notice.isDeleted());
    }

    @Test
    @DisplayName("공지사항 삭제 실패: 로그인 사용자와 공지사항 작성자가 다름")
    void testDeleteNotice_Fail_DifferentUsers() {
        // Given
        Long noticeId = 1L;
        User loginUser = TestDataFactory.generateUserWithId(2L);
        User originalNoticeUser = TestDataFactory.generateUserWithId(3L);

        Notice notice = TestDataFactory.generateNotice(originalNoticeUser,
                TestDataFactory.generateDefaultProject(originalNoticeUser.getId()));
        ReflectionTestUtils.setField(notice, "id", noticeId);

        // When/Then
        assertThrows(NoticePermissionException.class, () -> notice.deleteNotice(loginUser));
    }


}