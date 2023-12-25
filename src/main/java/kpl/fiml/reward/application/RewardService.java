package kpl.fiml.reward.application;

import kpl.fiml.reward.domain.RewardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RewardService {

    private final RewardRepository rewardRepository;
}
