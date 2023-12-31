package kpl.fiml.sponsor.presentation;

import jakarta.validation.Valid;
import kpl.fiml.sponsor.application.SponsorService;
import kpl.fiml.sponsor.dto.request.SponsorCreateRequest;
import kpl.fiml.sponsor.dto.request.SponsorUpdateRequest;
import kpl.fiml.sponsor.dto.response.SponsorCreateResponse;
import kpl.fiml.sponsor.dto.response.SponsorDeleteResponse;
import kpl.fiml.sponsor.dto.response.SponsorDto;
import kpl.fiml.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class SponsorController {

    private final SponsorService sponsorService;

    @PostMapping("/sponsors")
    public ResponseEntity<SponsorCreateResponse> createSponsor(@Valid @RequestBody SponsorCreateRequest request, @AuthenticationPrincipal User user) {
        SponsorCreateResponse response = sponsorService.createSponsor(request, user.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/sponsors")
    public ResponseEntity<List<SponsorDto>> getSponsorsByUser(@AuthenticationPrincipal User user) {
        List<SponsorDto> responses = sponsorService.getSponsorsByUser(user.getId());

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/sponsors/project/{projectId}")
    public ResponseEntity<List<SponsorDto>> getSponsorsByProject(@PathVariable Long projectId, @AuthenticationPrincipal User user) {
        List<SponsorDto> responses = sponsorService.getSponsorsByProject(projectId, user.getId());

        return ResponseEntity.ok(responses);
    }

    @PatchMapping("/sponsors/{sponsorId}")
    public ResponseEntity<SponsorDto> updateSponsor(@PathVariable Long sponsorId, @Valid @RequestBody SponsorUpdateRequest request, @AuthenticationPrincipal User user) {
        SponsorDto response = sponsorService.updateSponsor(sponsorId, request, user.getId());

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/sponsors/{sponsorId}")
    public ResponseEntity<SponsorDeleteResponse> deleteSponsor(@PathVariable Long sponsorId, @AuthenticationPrincipal User user) {
        SponsorDeleteResponse response = sponsorService.deleteSponsorByUser(sponsorId, user.getId());

        return ResponseEntity.ok(response);
    }
}
