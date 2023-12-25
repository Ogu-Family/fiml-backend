package kpl.fiml.reward.presentation;

import kpl.fiml.reward.application.RewardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class RewardController {

    private final RewardService rewardService;
}
