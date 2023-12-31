package kpl.fiml.user.application;

import kpl.fiml.global.jwt.JwtTokenProvider;
import kpl.fiml.user.domain.User;
import kpl.fiml.user.domain.UserRepository;
import kpl.fiml.user.dto.LoginRequest;
import kpl.fiml.user.dto.LoginResponse;
import kpl.fiml.user.dto.UserCreateRequest;
import kpl.fiml.user.dto.UserCreateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public UserCreateResponse createUser(UserCreateRequest request) {
        User savedUser = userRepository.save(request.toEntity(passwordEncoder));

        return UserCreateResponse.of(savedUser.getId());
    }

    @Transactional
    public LoginResponse signIn(LoginRequest request) {
        User user = userRepository.findByEmailAndDeletedAtIsNull(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("가입된 E-MAIL이 아닙니다."));
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        String jwtToken = jwtTokenProvider.generateToken(user.getUsername(), user.getRoles());

        return LoginResponse.of(jwtToken, user.getName(), user.getEmail());
    }

}
