package kpl.fiml.project.exception.reward;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RewardErrorCode {

    REWARD_NOT_FOUND("리워드 정보가 존재하지 않습니다."),
    INVALID_QUANTITY("최소 구매 수량은 1개 이상이어야 합니다."),
    INVALID_QUANTITY_LIMIT("수량 제한이 있는 리워드는 최소 1개 이상이어야 합니다."),
    INVALID_PRICE("리워드 가격은 0원 이상의 정수여야 합니다.");

    private final String message;
}
