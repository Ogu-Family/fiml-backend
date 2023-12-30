package kpl.fiml.sponsor.presentation;

import kpl.fiml.sponsor.application.SponsorService;
import kpl.fiml.sponsor.dto.SponsorCreateRequest;
import kpl.fiml.sponsor.dto.SponsorCreateResponse;
import kpl.fiml.sponsor.dto.SponsorDeleteResponse;
import kpl.fiml.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class SponsorController {

    private final SponsorService sponsorService;

    @PostMapping("/sponsors")
    public ResponseEntity<SponsorCreateResponse> createSponsor(@RequestBody SponsorCreateRequest request, @AuthenticationPrincipal User user) {
        SponsorCreateResponse response = sponsorService.createSponsor(request, user);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/sponsors/{sponsorId}")
    public ResponseEntity<SponsorDeleteResponse> deleteSponsor(@PathVariable Long sponsorId, @AuthenticationPrincipal User user) {
        SponsorDeleteResponse response = sponsorService.deleteSponsorByUser(sponsorId, user);

        return ResponseEntity.ok(response);
    }
}
