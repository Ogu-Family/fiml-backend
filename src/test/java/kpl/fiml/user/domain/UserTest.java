package kpl.fiml.user.domain;

import kpl.fiml.TestDataFactory;
import kpl.fiml.user.exception.*;
import kpl.fiml.user.vo.ContactVo;
import kpl.fiml.user.vo.EmailVo;
import kpl.fiml.user.vo.PasswordVo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    @DisplayName("사용자 캐시 증가 성공: 유효한 금액")
    void testIncreaseCash_Success_ValidAmount() {
        // Given
        User user = TestDataFactory.generateUserWithId(1L);

        // When
        user.increaseCash(100L);

        // Then
        assertEquals(100L, user.getCash());
    }

    @Test
    @DisplayName("사용자 캐시 감소 성공: 유효한 금액")
    void testDecreaseCash_Success_ValidAmount() {
        // Given
        User user = TestDataFactory.generateUserWithId(1L);
        ReflectionTestUtils.setField(user, "cash", 200L);

        // When
        user.decreaseCash(100L);

        // Then
        assertEquals(100L, user.getCash());
    }

    @Test
    @DisplayName("사용자 캐시 감소 실패: 부적절한 금액")
    void testDecreaseCash_Fail_InvalidAmount() {
        // Given
        User user = TestDataFactory.generateUserWithId(1L);

        // When/Then
        assertThrows(InvalidAmountException.class, () -> user.decreaseCash(-100L), "부적절한 금액으로 인해 예외가 발생해야 합니다.");
    }

    @Test
    @DisplayName("사용자 캐시 감소 실패: 캐시 부족")
    void testDecreaseCash_Fail_NotEnoughCash() {
        // Given
        User user = TestDataFactory.generateUserWithId(1L);
        ReflectionTestUtils.setField(user, "cash", 50L);

        // When/Then
        assertThrows(CashNotEnoughException.class, () -> user.decreaseCash(100L), "캐시 부족으로 인해 예외가 발생해야 합니다.");
    }

    @Test
    @DisplayName("사용자 정보 업데이트 성공: 유효한 업데이트")
    void testUpdateUser_Success_ValidUpdate() {
        // Given
        User user = TestDataFactory.generateUserWithId(1L);

        // When
        user.updateUser(user, "Updated Name", "Updated Bio", "new-image.jpg", "update@example.com", "newpassword123!", "0101234567");

        // Then
        assertEquals("Updated Name", user.getName(), "사용자 이름이 정상적으로 업데이트되었어야 합니다.");
        assertEquals("Updated Bio", user.getBio(), "사용자 Bio가 정상적으로 업데이트되었어야 합니다.");
        assertEquals("new-image.jpg", user.getProfileImage(), "프로필 이미지가 정상적으로 업데이트되었어야 합니다.");
        assertEquals("update@example.com", user.getEmail(), "이메일이 정상적으로 업데이트되었어야 합니다.");
        assertEquals("newpassword123!", user.getPassword(), "비밀번호가 정상적으로 업데이트되었어야 합니다.");
        assertEquals("0101234567", user.getContact(), "연락처가 정상적으로 업데이트되었어야 합니다.");
    }

    @Test
    @DisplayName("사용자 정보 업데이트 실패: 접근 권한 없음")
    void testUpdateUser_Fail_AccessDenied() {
        // Given
        User user = TestDataFactory.generateUserWithId(1L);
        User loginUser = TestDataFactory.generateUserWithId(2L);

        // When/Then
        assertThrows(UserPermissionException.class, () -> user.updateUser(loginUser, "Updated Name", "Updated Bio", "new-image.jpg", "update@example.com", "newpassword123!", "0101234567"), "접근 권한이 없어야 합니다.");
    }

    @Test
    @DisplayName("연락처 생성 실패 : 특수문자 포함 불가")
    void testInvalidContact_Fail_SpecialCharacters() {
        // Given
        String invalidContact = "010-1234-5678";

        // When/Then
        assertThrows(InvalidContactException.class, () -> new ContactVo(invalidContact), "특수 문자가 포함된 연락처는 예외를 발생시켜야 합니다.");
    }

    @Test
    @DisplayName("이메일 생성 실패 : 유효하지 않은 이메일 형식")
    void testInvalidEmail_Fail_InvalidFormat() {
        // Given
        String invalidEmail = "invalid-email";

        // When/Then
        assertThrows(InvalidEmailException.class, () -> new EmailVo(invalidEmail), "유효하지 않은 이메일 형식은 예외를 발생시켜야 합니다.");
    }

    @Test
    @DisplayName("비밀번호 생성 실패 : 유효하지 않은 비밀번호")
    void testInvalidPassword_Fail_NoSpecialCharacters() {
        // Given
        String invalidPassword = "NoSpecial123";

        // When/Then
        assertThrows(InvalidPasswordException.class, () -> new PasswordVo(invalidPassword), "특수 문자를 포함하지 않은 비밀번호는 예외를 발생시켜야 합니다.");
    }
}