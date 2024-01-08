package kpl.fiml.sponsor.domain;

import kpl.fiml.project.domain.Reward;
import kpl.fiml.sponsor.exception.InvalidTotalAmountException;
import kpl.fiml.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class SponsorTest {

    @Test
    @DisplayName("후원 생성 시 리워드 가격보다 적은 후원 금액은 예외를 발생시킨다.")
    void testInvalidTotalAmountException() {
        // given
        User user = User.builder()
                .email("abc@gmail.com")
                .contact("01012345678")
                .build();
        Reward reward = Reward.builder()
                .quantityLimited(false)
                .maxPurchaseQuantity(100000)
                .price(50000L)
                .build();

        Long totalAmount = 49999L;

        // when-then
        assertThatThrownBy(() -> Sponsor.builder().user(user).reward(reward).totalAmount(totalAmount).build())
                .isInstanceOf(InvalidTotalAmountException.class)
                .hasMessageContaining("후원 금액이 리워드 가격보다 적습니다.");
    }

}
