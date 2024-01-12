package kpl.fiml.sponsor.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "Sponsor Controller", description = "후원 컨트롤러")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class SponsorController {

    private final SponsorService sponsorService;

    @Operation(summary = "후원 생성", description = "특정 리워드에 대한 후원 생성")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "후원 생성 성공"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 회원으로 요청됨"),
            @ApiResponse(responseCode = "403", description = "접근 권한이 없는 회원으로 요청됨"),
            @ApiResponse(responseCode = "404", description = "1. 존재하지 않는 회원 아이디로 요청됨\n 2. 존재하지 않는 리워드 아이디로 요청됨")
    })
    @PostMapping("/sponsors")
    public ResponseEntity<SponsorCreateResponse> createSponsor(@Valid @RequestBody SponsorCreateRequest request, @AuthenticationPrincipal User user) {
        SponsorCreateResponse response = sponsorService.createSponsor(request, user.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "후원 조회", description = "특정 회원에 대한 모든 후원 내역 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "후원 내역 조회 성공"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 회원으로 요청됨"),
            @ApiResponse(responseCode = "403", description = "접근 권한이 없는 회원으로 요청됨"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 회원 아이디로 요청됨")
    })
    @GetMapping("/sponsors")
    public ResponseEntity<List<SponsorDto>> getSponsorsByUser(@AuthenticationPrincipal User user) {
        List<SponsorDto> responses = sponsorService.getSponsorsByUser(user.getId());

        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "후원 조회", description = "특정 프로젝트에 대한 모든 후원 내역 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "후원 내역 조회 성공"),
            @ApiResponse(responseCode = "400", description = "다른 창작자의 프로젝트에 대한 후원 내역 조회 시도"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 회원으로 요청됨"),
            @ApiResponse(responseCode = "403", description = "접근 권한이 없는 회원으로 요청됨"),
            @ApiResponse(responseCode = "404", description = "1. 존재하지 않는 회원 아이디로 요청됨\n 2. 존재하지 않는 프로젝트 아이디로 요청됨")
    })
    @Parameters({
            @Parameter(name = "projectId", description = "프로젝트 아이디")
    })
    @GetMapping("/sponsors/project/{projectId}")
    public ResponseEntity<List<SponsorDto>> getSponsorsByProject(@PathVariable Long projectId, @AuthenticationPrincipal User user) {
        List<SponsorDto> responses = sponsorService.getSponsorsByProject(projectId, user.getId());

        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "후원 수정", description = "특정 후원에 대한 정보 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "후원 정보 수정 성공"),
            @ApiResponse(responseCode = "400", description = "1. 다른 후원자의 후원에 대한 정보 수정 시도\n 2. 이미 펀딩 종료된 프로젝트에 대한 후원 수정 시도"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 회원으로 요청됨"),
            @ApiResponse(responseCode = "403", description = "접근 권한이 없는 회원으로 요청됨"),
            @ApiResponse(responseCode = "404", description = "1. 존재하지 않는 회원 아이디로 요청됨\n 2. 존재하지 않는 후원 아이디로 요청됨\n 3. 존재하지 않는 리워드 아이디로 요청됨")
    })
    @Parameters({
            @Parameter(name = "sponsorId", description = "후원 아이디")
    })
    @PatchMapping("/sponsors/{sponsorId}")
    public ResponseEntity<SponsorDto> updateSponsor(@PathVariable Long sponsorId, @Valid @RequestBody SponsorUpdateRequest request, @AuthenticationPrincipal User user) {
        SponsorDto response = sponsorService.updateSponsor(sponsorId, request, user.getId());

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "후원 삭제", description = "특정 후원에 대한 취소(삭제)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "후원 취소 성공"),
            @ApiResponse(responseCode = "400", description = "1. 다른 후원자의 후원에 대한 취소 시도\n 2. 이미 펀딩 종료된 프로젝트에 대한 후원 취소 시도"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 회원으로 요청됨"),
            @ApiResponse(responseCode = "403", description = "접근 권한이 없는 회원으로 요청됨"),
            @ApiResponse(responseCode = "404", description = "1. 존재하지 않는 회원 아이디로 요청됨\n 2. 존재하지 않는 후원 아이디로 요청됨")
    })
    @Parameters({
            @Parameter(name = "sponsorId", description = "후원 아이디")
    })
    @DeleteMapping("/sponsors/{sponsorId}")
    public ResponseEntity<SponsorDeleteResponse> deleteSponsor(@PathVariable Long sponsorId, @AuthenticationPrincipal User user) {
        SponsorDeleteResponse response = sponsorService.deleteSponsorByUser(sponsorId, user.getId());

        return ResponseEntity.ok(response);
    }
}
