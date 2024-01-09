package kpl.fiml.project.exception.reward;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RewardErrorCode {

    REWARD_NOT_FOUND("리워드 정보가 존재하지 않습니다.");

    private final String message;
}
