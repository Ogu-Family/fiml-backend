package kpl.fiml.user.application;

import kpl.fiml.global.jwt.JwtTokenProvider;
import kpl.fiml.user.domain.Following;
import kpl.fiml.user.domain.FollowingRepository;
import kpl.fiml.user.domain.User;
import kpl.fiml.user.domain.UserRepository;
import kpl.fiml.user.dto.*;
import kpl.fiml.user.dto.request.LoginRequest;
import kpl.fiml.user.dto.request.UserCreateRequest;
import kpl.fiml.user.dto.request.UserUpdateRequest;
import kpl.fiml.user.dto.response.LoginResponse;
import kpl.fiml.user.dto.response.UserCreateResponse;
import kpl.fiml.user.dto.response.UserDeleteResponse;
import kpl.fiml.user.dto.response.UserUpdateResponse;
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
    private final FollowingRepository followingRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public UserCreateResponse createUser(UserCreateRequest request) {
        // email 중복 확인
        if (userRepository.existsByEmailAndDeletedAtIsNull(request.getEmail())) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }

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
    public UserUpdateResponse updateUser(Long id, Long loginUserId, UserUpdateRequest request) {
        User user = getById(id);
        User loginUser = getById(loginUserId);

        if (!user.isSameUser(loginUser)) {
            throw new IllegalArgumentException("사용자 수정 권한이 없습니다.");
        }

        String encryptPassword = request.getPassword();
        if (!encryptPassword.isBlank()) {
            encryptPassword = validateAndEncryptPassword(request.getPassword());
        }
        user.updateUser(request.getName(), request.getBio(), request.getProfileImage(), request.getEmail(), encryptPassword, request.getContact());

        return UserUpdateResponse.of(user.getId(), user.getName(), user.getBio(), user.getProfileImage(), user.getEmail(), user.getPassword(), user.getContact());
    }

    public UserDto findById(Long userId, Long loginUserId) {
        User findUser = getById(userId);
        User loginUser = getById(loginUserId);

        if (!findUser.isSameUser(loginUser)) {
            throw new IllegalArgumentException("마이페이지 접근 권한이 없습니다.");
        }

        return UserDto.of(findUser.getId(), findUser.getName(), findUser.getBio(), findUser.getProfileImage(), findUser.getEmail(), findUser.getContact(), findUser.getCash(), findUser.getCreatedAt(), findUser.getUpdatedAt());
    }

    @Transactional
    public UserDeleteResponse deleteById(Long userId, Long loginUserId) {
        User user = getById(userId);
        User loginUser = getById(loginUserId);

        if (!user.isSameUser(loginUser)) {
            throw new IllegalArgumentException("사용자 삭제 권한이 없습니다.");
        }
        user.deleteUser();

        return UserDeleteResponse.of(user.getId());
    }

    private String validateAndEncryptPassword(String password) {
        String rawPassword = new PasswordVo(password).getPassword();
        return passwordEncoder.encode(rawPassword);
    }

    public User getById(Long userId) {
        return userRepository.findByIdAndDeletedAtIsNull(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자 입니다."));
    }

    @Transactional
    public void follow(Long followingId, Long followerId) {
        User following = this.getById(followingId);
        User follower = this.getById(followerId);

        checkIfAlreadyFollowing(following, follower);

        followingRepository.save(
                Following.builder()
                        .followingUser(following)
                        .followerUser(follower)
                        .build()
        );
    }

    private void checkIfAlreadyFollowing(User following, User follower) {
        if (followingRepository.existsByFollowingUserAndFollowerUser(following, follower)) {
            throw new IllegalArgumentException("이미 팔로우한 사용자 입니다.");
        }
    }

    @Transactional
    public void unfollow(Long followingId, Long followerId) {
        User following = this.getById(followingId);
        User follower = this.getById(followerId);

        Following findFollowing = getFollowingByFollowInfo(following, follower);

        followingRepository.delete(findFollowing);

    }

    private Following getFollowingByFollowInfo(User following, User follower) {
        return followingRepository.findByFollowingUserAndFollowerUser(following, follower)
                .orElseThrow(() -> new IllegalArgumentException("팔로우하지 않은 사용자 입니다."));
    }
}
