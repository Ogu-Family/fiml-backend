package kpl.fiml.user.presentation;

import kpl.fiml.user.application.UserService;
import kpl.fiml.user.domain.User;
import kpl.fiml.user.dto.*;
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
    public ResponseEntity<UserCreateResponse> createUser(@RequestBody UserCreateRequest request) {
        UserCreateResponse response = userService.createUser(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/users/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse response = userService.signIn(request);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping("/users/{id}")
    public ResponseEntity<UserUpdateResponse> update(@PathVariable Long id, @RequestBody UserUpdateRequest request) {
        UserUpdateResponse response = userService.updateUser(id, request);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserDto> findById(@PathVariable Long id) {
        UserDto response = userService.findById(id);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<UserDeleteResponse> deleteById(@PathVariable Long id) {
        UserDeleteResponse response = userService.deleteById(id);

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
