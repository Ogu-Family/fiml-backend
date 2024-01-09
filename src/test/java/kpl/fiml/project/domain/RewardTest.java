package kpl.fiml.project.domain;

import kpl.fiml.project.exception.reward.RewardFieldValueException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.util.stream.Stream;

import static kpl.fiml.TestDataFactory.generateReward;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RewardTest {

    private static Stream<Arguments> provideValidReward() {
        return Stream.of(
                Arguments.of(1, 10000L, true, 10, LocalDate.of(2022, 12, 31), 2),
                Arguments.of(1, 10000L, false, 10, LocalDate.of(2022, 12, 31), 2),
                Arguments.of(1, 0L, false, 10, LocalDate.of(2022, 12, 31), 2)
        );
    }

    private static Stream<Arguments> provideInvalidQuantityLimited() {
        return Stream.of(
                Arguments.of(true, 0),
                Arguments.of(true, -1),
                Arguments.of(true, -100)
        );
    }

    private static Stream<Arguments> provideInvalidMaxPurchaseQuantity() {
        return Stream.of(
                Arguments.of(0),
                Arguments.of(-1),
                Arguments.of(-100)
        );
    }

    private static Stream<Arguments> provideNotQuantityLimited() {
        return Stream.of(
                Arguments.of(false, 10),
                Arguments.of(false, 100),
                Arguments.of(false, 1000)
        );
    }

    private static Stream<Arguments> provideInvalidPrice() {
        return Stream.of(
                Arguments.of(-1L),
                Arguments.of(-100L)
        );
    }

    private static Stream<Arguments> provideCheckUnderflowPrice() {
        return Stream.of(
                Arguments.of(10000L, 500L, true),
                Arguments.of(10000L, 10000L, false),
                Arguments.of(10000L, 10001L, false),
                Arguments.of(10000L, 10002L, false)
        );
    }

    @ParameterizedTest
    @MethodSource("provideValidReward")
    @DisplayName("Reward 객체 생성에 성공합니다.")
    void testCreateReward_Success(Integer sequence, Long price, Boolean quantityLimited, Integer totalQuantity, LocalDate deliveryDate, Integer maxPurchaseQuantity) {
        // Given

        // When
        Reward reward = generateReward(sequence, price, quantityLimited, totalQuantity, deliveryDate, maxPurchaseQuantity);

        // Then
        assertThat(reward).isNotNull();
    }

    @ParameterizedTest
    @MethodSource("provideInvalidQuantityLimited")
    @DisplayName("Reward 객체 생성 시 제한 수량이 0 이하일 때 예외가 발생합니다.")
    void testCreateReward_QuantityLimitException(Boolean quantityLimited, Integer totalQuantity) {
        // Given
        Integer sequence = 1;
        Long price = 10000L;
        LocalDate deliveryDate = LocalDate.of(2022, 12, 31);
        Integer maxPurchaseQuantity = 2;

        // When, Then
        assertThatThrownBy(() -> generateReward(sequence, price, quantityLimited, totalQuantity, deliveryDate, maxPurchaseQuantity))
                .isInstanceOf(RewardFieldValueException.class);
    }

    @ParameterizedTest
    @MethodSource("provideInvalidMaxPurchaseQuantity")
    @DisplayName("Reward 객체 생성 시 최소 구매 수량이 0 이하일 때 예외가 발생합니다.")
    void testCreateReward_MinPurchaseQuantityException(Integer maxPurchaseQuantity) {
        // Given
        Integer sequence = 1;
        Long price = 10000L;
        Boolean quantityLimited = true;
        Integer totalQuantity = 10;
        LocalDate deliveryDate = LocalDate.of(2022, 12, 31);

        // When, Then
        assertThatThrownBy(() -> generateReward(sequence, price, quantityLimited, totalQuantity, deliveryDate, maxPurchaseQuantity))
                .isInstanceOf(RewardFieldValueException.class);
    }

    @ParameterizedTest
    @MethodSource("provideNotQuantityLimited")
    @DisplayName("Reward 객체 생성 시 수량 무제한일 때 remainQuantity는 Integer.MAX_VALUE로 설정됩니다.")
    void testCreateReward_UnlimitedQuantity_Success(Boolean quantityLimited, Integer totalQuantity) {
        // Given
        Integer sequence = 1;
        Long price = 10000L;
        LocalDate deliveryDate = LocalDate.of(2022, 12, 31);
        Integer maxPurchaseQuantity = 2;

        // When
        Reward reward = generateReward(sequence, price, quantityLimited, totalQuantity, deliveryDate, maxPurchaseQuantity);

        // Then
        assertThat(reward.getRemainQuantity()).isEqualTo(Integer.MAX_VALUE);
    }

    @ParameterizedTest
    @MethodSource("provideInvalidPrice")
    @DisplayName("Reward 객체 생성 시 가격이 0 이하일 때 예외가 발생합니다.")
    void testCreateReward_PriceException(Long price) {
        // Given
        Integer sequence = 1;
        Boolean quantityLimited = true;
        Integer totalQuantity = 10;
        LocalDate deliveryDate = LocalDate.of(2022, 12, 31);
        Integer maxPurchaseQuantity = 2;

        // When, Then
        assertThatThrownBy(() -> generateReward(sequence, price, quantityLimited, totalQuantity, deliveryDate, maxPurchaseQuantity))
                .isInstanceOf(RewardFieldValueException.class);
    }

    @ParameterizedTest
    @MethodSource("provideCheckUnderflowPrice")
    @DisplayName("Reward 객체의 가격이 구매 금액보다 작을 때 true를 반환합니다.")
    void testCheckUnderflowPrice_Success(Long price, Long amount, Boolean expected) {
        // Given
        Integer sequence = 1;
        Boolean quantityLimited = true;
        Integer totalQuantity = 10;
        LocalDate deliveryDate = LocalDate.of(2022, 12, 31);
        Integer maxPurchaseQuantity = 2;
        Reward reward = generateReward(sequence, price, quantityLimited, totalQuantity, deliveryDate, maxPurchaseQuantity);

        // When
        Boolean result = reward.checkUnderflowPrice(amount);

        // Then
        assertThat(result).isEqualTo(expected);
    }
}
