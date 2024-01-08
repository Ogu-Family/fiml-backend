package kpl.fiml.user.application;

import kpl.fiml.global.jwt.JwtTokenProvider;
import kpl.fiml.user.domain.UserRepository;
import kpl.fiml.user.dto.request.UserCreateRequest;
import kpl.fiml.user.dto.response.UserCreateResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

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
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("user 생성에 성공합니다.")
    public void testCreateUser_success() {
        UserCreateRequest request = UserCreateRequest.builder()
                .bio("")
                .contact("01012345678")
                .email("test@example.com")
                .name("Test Name")
                .password("password123!")
                .profileImage("")
                .build();

        when(userRepository.existsByEmailAndDeletedAtIsNull("test@example.com")).thenReturn(false);

        String dummyEncryptedPassword = "dummyEncryptedPassword";
        when(userRepository.save(any())).thenReturn(request.toEntity(dummyEncryptedPassword));

        UserCreateResponse response = userService.createUser(request);

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
        assertThrows(IllegalArgumentException.class, () -> userService.createUser(request));
    }
}
