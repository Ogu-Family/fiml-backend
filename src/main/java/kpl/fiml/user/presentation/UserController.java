package kpl.fiml.user.presentation;

import kpl.fiml.user.application.UserService;
import kpl.fiml.user.dto.UserCreateRequest;
import kpl.fiml.user.dto.UserCreateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
