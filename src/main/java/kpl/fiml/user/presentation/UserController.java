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
    public ResponseEntity<UserUpdateResponse> update(@PathVariable Long id, @RequestBody UserUpdateRequest request,
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

    @PostMapping("/users/test")
    public String test() {
        // security ROLE_USER 권한 확인용 api
        return "success";
    }
}
