package kpl.fiml.user.presentation;

import jakarta.validation.Valid;
import kpl.fiml.user.application.UserService;
import kpl.fiml.user.domain.User;
import kpl.fiml.user.dto.*;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserController {

    private final UserService userService;

    @PostMapping("/users/join")
    public ResponseEntity<UserCreateResponse> createUser(@Valid @RequestBody UserCreateRequest request) {
        UserCreateResponse response = userService.createUser(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/users/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = userService.signIn(request);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping("/users/{id}")
    public ResponseEntity<UserUpdateResponse> update(@PathVariable Long id, @Valid @RequestBody UserUpdateRequest request,
                                                     @AuthenticationPrincipal User loginUser) {
        UserUpdateResponse response = userService.updateUser(id, loginUser.getId(), request);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserDto> findById(@PathVariable Long id, @AuthenticationPrincipal User loginUser) {
        UserDto response = userService.findById(id, loginUser.getId());

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

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

    @PostMapping("/users/test")
    public String test() {
        // security ROLE_USER 권한 확인용 api
        return "success";
    }
}
