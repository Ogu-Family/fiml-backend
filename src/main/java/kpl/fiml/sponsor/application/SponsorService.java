package kpl.fiml.sponsor.application;

import kpl.fiml.payment.application.PaymentService;
import kpl.fiml.payment.dto.PaymentCreateRequest;
import kpl.fiml.project.domain.Project;
import kpl.fiml.project.domain.Reward;
import kpl.fiml.project.domain.RewardRepository;
import kpl.fiml.sponsor.domain.Sponsor;
import kpl.fiml.sponsor.domain.SponsorRepository;
import kpl.fiml.sponsor.dto.SponsorCreateRequest;
import kpl.fiml.sponsor.dto.SponsorCreateResponse;
import kpl.fiml.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SponsorService {

    private final PaymentService paymentService;

    private final SponsorRepository sponsorRepository;
    private final RewardRepository rewardRepository;

    @Transactional
    public SponsorCreateResponse createSponsor(SponsorCreateRequest request, User user) {
        Reward reward = rewardRepository.findById(request.getRewardId())
                .filter(uncheckedReward -> !uncheckedReward.isDeleted())
                .orElseThrow(() -> new IllegalArgumentException("해당하는 리워드가 존재하지 않습니다."));

        Sponsor sponsor = sponsorRepository.save(request.toEntity(user, reward));

        paymentService.createPayment(PaymentCreateRequest.of(sponsor, reward.getProject().getEndAt().toLocalDate()));

        return SponsorCreateResponse.of(sponsor.getId());
    }

    @Transactional
    public void deleteSponsors(Project project) {
        List<Reward> rewards = rewardRepository.findAllByProject(project);

        for (Reward reward : rewards) {
            List<Sponsor> sponsors = sponsorRepository.findAllByReward(reward);

            for (Sponsor sponsor : sponsors) {
                paymentService.deletePayments(sponsor);
                sponsor.delete();
            }
        }
    }
}
