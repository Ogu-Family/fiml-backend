package kpl.fiml.sponsor.presentation;

import kpl.fiml.sponsor.application.SponsorService;
import kpl.fiml.sponsor.dto.*;
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
        SponsorCreateResponse response = sponsorService.createSponsor(request, user.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/sponsors/{sponsorId}")
    public ResponseEntity<SponsorDto> getSponsor(@PathVariable Long sponsorId, @AuthenticationPrincipal User user) {
        SponsorDto response = sponsorService.getSponsor(sponsorId, user.getId());

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/sponsors/{sponsorId}")
    public ResponseEntity<SponsorDto> updateSponsor(@PathVariable Long sponsorId, @RequestBody SponsorUpdateRequest request, @AuthenticationPrincipal User user) {
        SponsorDto response = sponsorService.updateSponsor(sponsorId, request, user.getId());

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/sponsors/{sponsorId}")
    public ResponseEntity<SponsorDeleteResponse> deleteSponsor(@PathVariable Long sponsorId, @AuthenticationPrincipal User user) {
        SponsorDeleteResponse response = sponsorService.deleteSponsorByUser(sponsorId, user.getId());

        return ResponseEntity.ok(response);
    }
}
