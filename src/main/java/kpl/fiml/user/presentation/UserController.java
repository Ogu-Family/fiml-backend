package kpl.fiml.user.presentation;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kpl.fiml.user.application.UserService;
import kpl.fiml.user.domain.User;
import kpl.fiml.user.dto.UserDto;
import kpl.fiml.user.dto.request.LoginRequest;
import kpl.fiml.user.dto.request.UserCreateRequest;
import kpl.fiml.user.dto.request.UserUpdateRequest;
import kpl.fiml.user.dto.response.LoginResponse;
import kpl.fiml.user.dto.response.UserCreateResponse;
import kpl.fiml.user.dto.response.UserDeleteResponse;
import kpl.fiml.user.dto.response.UserUpdateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User Controller", description = "사용자 컨트롤러")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserController {

    private final UserService userService;

    @Operation(summary = "회원 가입", description = "회원가입을 합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원가입 성공"),
            @ApiResponse(responseCode = "400", description = "1. 이미 가입된 이메일로 회원가입을 시도\n2. 올바르지 않은 이메일 또는 비밀번호 또는 연락처 형식")
    })
    @PostMapping("/users/join")
    public ResponseEntity<UserCreateResponse> createUser(@Valid @RequestBody UserCreateRequest request) {
        UserCreateResponse response = userService.createUser(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "로그인", description = "이메일, 비밀번호로 로그인을 합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그인 성공 후 JWT 토큰 발급"),
            @ApiResponse(responseCode = "400", description = "올바르지 않은 비밀번호로 로그인 시도"),
            @ApiResponse(responseCode = "404", description = "가입되지 않은 이메일로 로그인 시도")
    })
    @PostMapping("/users/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = userService.signIn(request);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "사용자 정보 수정", description = "사용자의 정보를 업데이트 합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "사용자 정보 수정 성공"),
            @ApiResponse(responseCode = "400", description = "이메일, 비밀번호, 연락처 유효하지 않은 형식"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 사용자 id로 요청")
    })
    @Parameters({
            @Parameter(name = "id", description = "사용자 id")
    })
    @PatchMapping("/users/{id}")
    public ResponseEntity<UserUpdateResponse> update(@PathVariable Long id, @Valid @RequestBody UserUpdateRequest request,
                                                     @AuthenticationPrincipal User loginUser) {
        UserUpdateResponse response = userService.updateUser(id, loginUser.getId(), request);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "사용자 상세 정보 조회", description = "사용자의 정보를 조회 합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "사용자 정보 조회 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 사용자 id로 요청")
    })
    @Parameters({
            @Parameter(name = "id", description = "사용자 id")
    })
    @GetMapping("/users/{id}")
    public ResponseEntity<UserDto> findById(@PathVariable Long id, @AuthenticationPrincipal User loginUser) {
        UserDto response = userService.findById(id, loginUser.getId());

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "사용자 삭제", description = "사용자 삭제로 탈퇴 처리 됩니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "사용자 정보 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 사용자 id로 요청")
    })
    @Parameters({
            @Parameter(name = "id", description = "사용자 id")
    })
    @DeleteMapping("/users/{id}")
    public ResponseEntity<UserDeleteResponse> deleteById(@PathVariable Long id, @AuthenticationPrincipal User loginUser) {
        UserDeleteResponse response = userService.deleteById(id, loginUser.getId());

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/users/{followingId}/follow")
    public ResponseEntity<Void> follow(@PathVariable("followingId") Long followingId, @AuthenticationPrincipal User user) {
        this.userService.follow(followingId, user.getId());

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/users/{followingId}/unfollow")
    public ResponseEntity<Void> unfollow(@PathVariable("followingId") Long followingId, @AuthenticationPrincipal User user) {
        this.userService.unfollow(followingId, user.getId());

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Hidden
    @PostMapping("/users/test")
    public String test() {
        // security ROLE_USER 권한 확인용 api
        return "success";
    }
}
