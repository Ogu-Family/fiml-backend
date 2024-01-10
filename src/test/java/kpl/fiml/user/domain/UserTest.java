package kpl.fiml.user.domain;

import kpl.fiml.TestDataFactory;
import kpl.fiml.user.exception.CashNotEnoughException;
import kpl.fiml.user.exception.InvalidAmountException;
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

}