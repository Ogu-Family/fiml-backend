package kpl.fiml.user.application;

import kpl.fiml.TestDataFactory;
import kpl.fiml.global.jwt.JwtTokenProvider;
import kpl.fiml.user.domain.User;
import kpl.fiml.user.domain.UserRepository;
import kpl.fiml.user.dto.request.LoginRequest;
import kpl.fiml.user.dto.request.UserCreateRequest;
import kpl.fiml.user.dto.request.UserUpdateRequest;
import kpl.fiml.user.dto.response.LoginResponse;
import kpl.fiml.user.dto.response.UserCreateResponse;
import kpl.fiml.user.exception.DuplicateEmailException;
import kpl.fiml.user.exception.EmailNotFoundException;
import kpl.fiml.user.exception.InvalidPasswordException;
import kpl.fiml.user.exception.PasswordMismatchException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

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
}
