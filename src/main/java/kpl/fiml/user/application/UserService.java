package kpl.fiml.user.application;

import kpl.fiml.global.jwt.JwtTokenProvider;
import kpl.fiml.user.domain.User;
import kpl.fiml.user.domain.UserRepository;
import kpl.fiml.user.dto.*;
import kpl.fiml.user.vo.EmailVo;
import kpl.fiml.user.vo.PasswordVo;
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
        String encryptPassword = validateAndEncryptPassword(request.getPassword());
        User savedUser = userRepository.save(request.toEntity(encryptPassword));

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

    @Transactional
    public UserUpdateResponse updateUser(Long userId, UserUpdateRequest request) {
        User user = getById(userId);
        String encryptPassword = request.getPassword();
        if(!encryptPassword.isBlank()) {
            encryptPassword = validateAndEncryptPassword(request.getPassword());
        }
        user.updateUser(request.getName(), request.getBio(), request.getProfileImage(), request.getEmail(), encryptPassword, request.getContact());

        return UserUpdateResponse.of(user.getId(), user.getName(), user.getBio(), user.getProfileImage(), user.getEmail(), user.getPassword(), user.getContact());
    }

    public UserDto findById(Long userId) {
        User findUser = getById(userId);

        return UserDto.of(findUser.getId(), findUser.getName(), findUser.getBio(), findUser.getProfileImage(), findUser.getEmail(), findUser.getContact(), findUser.getCash(), findUser.getCreatedAt(), findUser.getUpdatedAt());
    }

    @Transactional
    public UserDeleteResponse deleteById(Long userId) {
        User user = getById(userId);
        user.delete();

        return UserDeleteResponse.of(user.getId());
    }

    private String validateAndEncryptPassword(String password) {
        String rawPassword = new PasswordVo(password).getPassword();
        return passwordEncoder.encode(rawPassword);
    }

    private User getById(Long userId) {
        return userRepository.findByIdAndDeletedAtIsNull(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자 입니다."));
    }

    @Transactional
    public void follow(Long followingId, Long followerId) {
        User following = userRepository.findByIdAndDeletedAtIsNull(followingId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자 입니다."));
        User follower = userRepository.findByIdAndDeletedAtIsNull(followerId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자 입니다."));

        follower.addFollowing(following);
    }
}
