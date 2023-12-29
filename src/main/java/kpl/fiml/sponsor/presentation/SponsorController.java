package kpl.fiml.sponsor.presentation;

import kpl.fiml.sponsor.application.SponsorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class SponsorController {

    private final SponsorService sponsorService;
}
