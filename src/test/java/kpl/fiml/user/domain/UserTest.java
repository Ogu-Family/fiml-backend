package kpl.fiml.user.domain;

import kpl.fiml.TestDataFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    @DisplayName("사용자 캐시 증가 성공")
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
}