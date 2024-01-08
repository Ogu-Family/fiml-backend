package kpl.fiml.sponsor.application;

import kpl.fiml.payment.application.PaymentService;
import kpl.fiml.payment.dto.request.PaymentCreateRequest;
import kpl.fiml.project.application.ProjectService;
import kpl.fiml.project.domain.Project;
import kpl.fiml.project.domain.Reward;
import kpl.fiml.project.domain.RewardRepository;
import kpl.fiml.project.exception.RewardErrorCode;
import kpl.fiml.project.exception.RewardNotFoundException;
import kpl.fiml.sponsor.domain.Sponsor;
import kpl.fiml.sponsor.domain.SponsorRepository;
import kpl.fiml.sponsor.dto.request.SponsorCreateRequest;
import kpl.fiml.sponsor.dto.request.SponsorUpdateRequest;
import kpl.fiml.sponsor.dto.response.SponsorCreateResponse;
import kpl.fiml.sponsor.dto.response.SponsorDeleteResponse;
import kpl.fiml.sponsor.dto.response.SponsorDto;
import kpl.fiml.sponsor.exception.SponsorAccessDeniedException;
import kpl.fiml.sponsor.exception.SponsorErrorCode;
import kpl.fiml.sponsor.exception.SponsorModifyDeniedException;
import kpl.fiml.sponsor.exception.SponsorNotFoundException;
import kpl.fiml.user.application.UserService;
import kpl.fiml.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SponsorService {

    private final ProjectService projectService;
    private final UserService userService;
    private final PaymentService paymentService;

    private final SponsorRepository sponsorRepository;
    private final RewardRepository rewardRepository;

    @Transactional
    public SponsorCreateResponse createSponsor(SponsorCreateRequest request, Long userId) {
        User user = userService.getById(userId);
        Reward reward = getRewardById(request.getRewardId());

        Sponsor sponsor = sponsorRepository.save(request.toEntity(user, reward));

        reward.getProject().updateSponsorAddInfo(sponsor.getTotalAmount());
        paymentService.createPayment(PaymentCreateRequest.of(sponsor, reward.getProject().getEndAt().toLocalDate()));

        return SponsorCreateResponse.of(sponsor.getId());
    }

    public List<SponsorDto> getSponsorsByUser(Long userId) {
        User user = userService.getById(userId);

        return sponsorRepository.findAllByUserAndDeletedAtIsNull(user)
                .stream()
                .map(sponsor -> SponsorDto.of(sponsor.getUser().getId(), sponsor.getReward().getId(), sponsor.getTotalAmount(), sponsor.getStatus().getDisplayName()))
                .toList();
    }

    public List<SponsorDto> getSponsorsByProject(Long projectId, Long userId) {
        User user = userService.getById(userId);
        Project project = projectService.getProjectByIdWithUser(projectId);

        validateIsSameUser(user, project.getUser());

        List<Reward> rewards = rewardRepository.findAllByProjectAndDeletedAtIsNull(project);
        List<SponsorDto> responses = new ArrayList<>();
        for (Reward reward : rewards) {
            responses.addAll(sponsorRepository.findAllByReward(reward)
                    .stream()
                    .map(sponsor -> SponsorDto.of(sponsor.getUser().getId(), sponsor.getReward().getId(), sponsor.getTotalAmount(), sponsor.getStatus().getDisplayName()))
                    .toList());
        }

        return responses;
    }

    @Transactional
    public SponsorDto updateSponsor(Long sponsorId, SponsorUpdateRequest request, Long userId) {
        User user = userService.getById(userId);
        Sponsor sponsor = getSponsorById(sponsorId);

        validateIsSameUser(user, sponsor.getUser());
        validateCanModify(sponsor);

        Reward reward = getRewardById(request.getRewardId());

        reward.getProject().updateCurrentAmount(sponsor.getTotalAmount() - request.getTotalAmount());
        sponsor.updateRewardAndTotalAmount(reward, request.getTotalAmount());

        return SponsorDto.of(sponsor.getUser().getId(), sponsor.getReward().getId(), sponsor.getTotalAmount(), sponsor.getStatus().getDisplayName());
    }

    @Transactional
    public SponsorDeleteResponse deleteSponsorByUser(Long sponsorId, Long userId) {
        User user = userService.getById(userId);
        Sponsor sponsor = getSponsorById(sponsorId);

        validateIsSameUser(user, sponsor.getUser());
        validateCanModify(sponsor);

        paymentService.deletePayments(sponsor);
        sponsor.deleteSponsor();

        return SponsorDeleteResponse.of(sponsor.getId(), sponsor.getDeletedAt());
    }

    @Transactional
    public void deleteSponsorsByProject(Project project) {
        List<Reward> rewards = rewardRepository.findAllByProjectAndDeletedAtIsNull(project);

        for (Reward reward : rewards) {
            List<Sponsor> sponsors = sponsorRepository.findAllByReward(reward);

            for (Sponsor sponsor : sponsors) {
                paymentService.deletePayments(sponsor);
                sponsor.updateStatusToFundingFail();
                sponsor.deleteSponsor();
            }
        }
    }

    private Sponsor getSponsorById(Long sponsorId) {
        return sponsorRepository.findByIdAndDeletedAtIsNull(sponsorId)
                .orElseThrow(() -> new SponsorNotFoundException(SponsorErrorCode.SPONSOR_NOT_FOUND));
    }

    private Reward getRewardById(Long rewardId) {
        return rewardRepository.findByIdAndDeletedAtIsNull(rewardId)
                .orElseThrow(() -> new RewardNotFoundException(RewardErrorCode.REWARD_NOT_FOUND));
    }

    private void validateIsSameUser(User user, User uncheckedUser) {
        if (!user.isSameUser(uncheckedUser)) {
            throw new SponsorAccessDeniedException(SponsorErrorCode.SPONSOR_ACCESS_DENIED);
        }
    }

    private void validateCanModify(Sponsor sponsor) {
        if (sponsor.getReward().getProject().getEndAt().isBefore(LocalDateTime.now())) {
            throw new SponsorModifyDeniedException(SponsorErrorCode.SPONSOR_MODIFY_DENIED);
        }
    }
}
