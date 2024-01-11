package kpl.fiml.user.application;

import kpl.fiml.TestDataFactory;
import kpl.fiml.global.jwt.JwtTokenProvider;
import kpl.fiml.user.domain.Following;
import kpl.fiml.user.domain.FollowingRepository;
import kpl.fiml.user.domain.User;
import kpl.fiml.user.domain.UserRepository;
import kpl.fiml.user.dto.request.LoginRequest;
import kpl.fiml.user.dto.request.UserCreateRequest;
import kpl.fiml.user.dto.request.UserUpdateRequest;
import kpl.fiml.user.dto.response.UserCreateResponse;
import kpl.fiml.user.exception.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private FollowingRepository followingRepository;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("user 생성에 성공합니다.")
    public void testCreateUser_success() {
        // Given
        UserCreateRequest request = UserCreateRequest.builder()
                .contact("01012345678")
                .email("test@example.com")
                .name("name")
                .password("password123!")
                .build();

        when(userRepository.existsByEmailAndDeletedAtIsNull("test@example.com")).thenReturn(false);

        String dummyEncryptedPassword = "dummyEncryptedPassword";
        when(userRepository.save(any())).thenReturn(request.toEntity(dummyEncryptedPassword));

        // When
        UserCreateResponse response = userService.createUser(request);

        // Then
        assertNotNull(response);
        verify(userRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("사용자 생성에 실패합니다.")
    public void testCreateUser_DuplicateEmail() {
        UserCreateRequest request = UserCreateRequest.builder()
                .bio("")
                .contact("01012345678")
                .email("test@example.com")
                .name("Test Name")
                .password("password123!")
                .profileImage("")
                .build();

        when(userRepository.existsByEmailAndDeletedAtIsNull("test@example.com")).thenReturn(true);
        assertThrows(DuplicateEmailException.class, () -> userService.createUser(request));
    }

    @Test
    @DisplayName("로그인 실패 : 가입되지 않은 이메일")
    void testSignIn_Fail_UserNotFound() {
        // Given
        String userEmail = "nonexistent@email.com";
        String userPassword = "password123!";

        when(userRepository.findByEmailAndDeletedAtIsNull(userEmail)).thenReturn(Optional.empty());

        // When
        LoginRequest loginRequest = LoginRequest.builder()
                .email(userEmail)
                .password(userPassword)
                .build();

        // Then
        assertThrows(EmailNotFoundException.class, () -> userService.signIn(loginRequest));

        // verify
        verify(userRepository, times(1)).findByEmailAndDeletedAtIsNull(userEmail);
        verify(passwordEncoder, never()).matches(anyString(), anyString()); // 비밀번호 검사는 일어나지 않아야 함
    }

    @Test
    @DisplayName("로그인 실패 : 잘못된 비밀번호")
    void testSignIn_Fail_PasswordMismatch() {
        // Given
        String userEmail = "email@email.com";
        String userPassword = "wrongPassword";
        User fakeUser = TestDataFactory.generateUserWithId(1L);

        when(userRepository.findByEmailAndDeletedAtIsNull(fakeUser.getEmail())).thenReturn(Optional.of(fakeUser));
        when(passwordEncoder.matches(userPassword, fakeUser.getPassword())).thenReturn(false);

        // When
        LoginRequest loginRequest = LoginRequest.builder()
                .email(userEmail)
                .password(userPassword)
                .build();

        // Then
        assertThrows(PasswordMismatchException.class, () -> userService.signIn(loginRequest));

        // verify
        verify(userRepository, times(1)).findByEmailAndDeletedAtIsNull(userEmail);
        verify(passwordEncoder, times(1)).matches(userPassword, fakeUser.getPassword());
    }

    @Test
    @DisplayName("user update 실패 : 잘못된 비밀번호 형식")
    void testUpdateUser_Fail_PasswordValidation() {
        // Given
        Long userId = 1L;
        Long loginUserId = 2L;
        UserUpdateRequest request = UserUpdateRequest.builder()
                .name("name")
                .profileImage("profile.jpg")
                .email("example@example.com")
                .password("password123")
                .contact("1234567890")
                .build();

        User fakeUser = TestDataFactory.generateUserWithId(userId);
        User fakeLoginUser = TestDataFactory.generateUserWithId(loginUserId);

        when(userRepository.findByIdAndDeletedAtIsNull(userId)).thenReturn(Optional.ofNullable(fakeUser));
        when(userRepository.findByIdAndDeletedAtIsNull(loginUserId)).thenReturn(Optional.ofNullable(fakeLoginUser));

        // When/Then
        assertThrows(InvalidPasswordException.class, () -> userService.updateUser(userId, loginUserId, request));

        // verify
        verify(userRepository, times(1)).findByIdAndDeletedAtIsNull(userId);
        verify(userRepository, times(1)).findByIdAndDeletedAtIsNull(loginUserId);
        verify(passwordEncoder, never()).encode(anyString()); // 비밀번호 암호화 메소드는 호출되지 않아야 함
    }

    @Test
    @DisplayName("사용자 마이페이지 조회 실패 : 접근 권한 없음")
    void testFindById_Fail_AccessDenied() {
        // Given
        Long userId = 1L;
        Long loginUserId = 2L;
        User fakeUser = TestDataFactory.generateUserWithId(userId);
        User loginUser = TestDataFactory.generateUserWithId(loginUserId);

        when(userRepository.findByIdAndDeletedAtIsNull(userId)).thenReturn(Optional.ofNullable(fakeUser));
        when(userRepository.findByIdAndDeletedAtIsNull(loginUserId)).thenReturn(Optional.ofNullable(loginUser));

        // When/Then
        assertThrows(UserPermissionException.class, () -> userService.findById(userId, loginUserId));

        // verify
        verify(userRepository, times(1)).findByIdAndDeletedAtIsNull(userId);
        verify(userRepository, times(1)).findByIdAndDeletedAtIsNull(loginUserId);
    }

    @Test
    @DisplayName("사용자 삭제 실패 : 접근 권한 없음")
    void testDeleteById_Fail_AccessDenied() {
        // Given
        Long userId = 1L;
        Long loginUserId = 2L;
        User fakeUser = TestDataFactory.generateUserWithId(userId);
        User loginUser = TestDataFactory.generateUserWithId(loginUserId);

        when(userRepository.findByIdAndDeletedAtIsNull(userId)).thenReturn(Optional.ofNullable(fakeUser));
        when(userRepository.findByIdAndDeletedAtIsNull(loginUserId)).thenReturn(Optional.ofNullable(loginUser));

        // When/Then
        assertThrows(UserPermissionException.class, () -> userService.deleteById(userId, loginUserId));

        // verify
        verify(userRepository, times(1)).findByIdAndDeletedAtIsNull(userId);
        verify(userRepository, times(1)).findByIdAndDeletedAtIsNull(loginUserId);
        assert fakeUser != null;
        verify(userRepository, never()).delete(fakeUser);
    }

    @Test
    @DisplayName("팔로우 성공")
    void testFollow_success() {
        // Given
        Long followingId = 1L;
        Long followerId = 2L;
        User following = TestDataFactory.generateUserWithId(followingId);
        User follower = TestDataFactory.generateUserWithId(followerId);

        when(userRepository.findByIdAndDeletedAtIsNull(followingId)).thenReturn(Optional.ofNullable(following));
        when(userRepository.findByIdAndDeletedAtIsNull(followerId)).thenReturn(Optional.ofNullable(follower));
        when(followingRepository.existsByFollowingUserAndFollowerUser(following, follower)).thenReturn(false);

        // When
        userService.follow(followingId, followerId);

        // Then
        verify(userRepository, times(1)).findByIdAndDeletedAtIsNull(followingId);
        verify(userRepository, times(1)).findByIdAndDeletedAtIsNull(followerId);
        verify(followingRepository, times(1)).existsByFollowingUserAndFollowerUser(following, follower);
        verify(followingRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("팔로우 실패 : 이미 팔로우한 사용자")
    void testFollow_fail_AlreadyFollowing() {
        // Given
        Long followingId = 1L;
        Long followerId = 2L;
        User following = TestDataFactory.generateUserWithId(followingId);
        User follower = TestDataFactory.generateUserWithId(followerId);

        when(userRepository.findByIdAndDeletedAtIsNull(followingId)).thenReturn(Optional.ofNullable(following));
        when(userRepository.findByIdAndDeletedAtIsNull(followerId)).thenReturn(Optional.ofNullable(follower));
        when(followingRepository.existsByFollowingUserAndFollowerUser(following, follower)).thenReturn(true);

        // When/Then
        assertThrows(FollowException.class, () -> userService.follow(followingId, followerId));

        // verify
        verify(userRepository, times(1)).findByIdAndDeletedAtIsNull(followingId);
        verify(userRepository, times(1)).findByIdAndDeletedAtIsNull(followerId);
        verify(followingRepository, times(1)).existsByFollowingUserAndFollowerUser(following, follower);
        verify(followingRepository, never()).save(any());
    }

    @Test
    @DisplayName("언팔로우 성공")
    void testUnfollow_success() {
        // Given
        Long followingId = 1L;
        Long followerId = 2L;
        User following = TestDataFactory.generateUserWithId(followingId);
        User follower = TestDataFactory.generateUserWithId(followerId);
        Following followingRelationship = Following.builder()
                .followingUser(following)
                .followerUser(follower)
                .build();

        when(userRepository.findByIdAndDeletedAtIsNull(followingId)).thenReturn(Optional.ofNullable(following));
        when(userRepository.findByIdAndDeletedAtIsNull(followerId)).thenReturn(Optional.ofNullable(follower));
        when(followingRepository.findByFollowingUserAndFollowerUser(following, follower)).thenReturn(Optional.ofNullable(followingRelationship));

        // When
        userService.unfollow(followingId, followerId);

        // Then
        verify(userRepository, times(1)).findByIdAndDeletedAtIsNull(followingId);
        verify(userRepository, times(1)).findByIdAndDeletedAtIsNull(followerId);
        verify(followingRepository, times(1)).findByFollowingUserAndFollowerUser(following, follower);
        verify(followingRepository, times(1)).delete(any());
    }

    @Test
    @DisplayName("언팔로우 실패 : 팔로우하지 않은 사용자")
    void testUnfollow_fail_NotFollowing() {
        // Given
        Long followingId = 1L;
        Long followerId = 2L;
        User following = TestDataFactory.generateUserWithId(followingId);
        User follower = TestDataFactory.generateUserWithId(followerId);

        when(userRepository.findByIdAndDeletedAtIsNull(followingId)).thenReturn(Optional.ofNullable(following));
        when(userRepository.findByIdAndDeletedAtIsNull(followerId)).thenReturn(Optional.ofNullable(follower));
        when(followingRepository.findByFollowingUserAndFollowerUser(following, follower)).thenReturn(Optional.empty());

        // When/Then
        assertThrows(FollowException.class, () -> userService.unfollow(followingId, followerId));

        // verify
        verify(userRepository, times(1)).findByIdAndDeletedAtIsNull(followingId);
        verify(userRepository, times(1)).findByIdAndDeletedAtIsNull(followerId);
        verify(followingRepository, times(1)).findByFollowingUserAndFollowerUser(following, follower);
        verify(followingRepository, never()).delete(any());
    }
}
