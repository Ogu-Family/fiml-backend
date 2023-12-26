package kpl.fiml.sponsor.application;

import kpl.fiml.sponsor.domain.SponsorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SponsorService {

    private final SponsorRepository sponsorRepository;
}
