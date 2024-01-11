package kpl.fiml.user.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import kpl.fiml.TestDataFactory;
import kpl.fiml.customMockUser.WithCustomMockUser;
import kpl.fiml.user.application.UserService;
import kpl.fiml.user.domain.Following;
import kpl.fiml.user.domain.FollowingRepository;
import kpl.fiml.user.domain.User;
import kpl.fiml.user.domain.UserRepository;
import kpl.fiml.user.dto.request.LoginRequest;
import kpl.fiml.user.dto.request.UserCreateRequest;
import kpl.fiml.user.exception.UserErrorCode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static kpl.fiml.TestDataFactory.generateLoginUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FollowingRepository followingRepository;

    @Autowired
    private UserService userService;

    @BeforeEach
    void setUp() {
        followingRepository.deleteAll();
        userRepository.deleteAll();
    }

    @AfterEach
    void cleanUp() {
        followingRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("회원가입 성공")
    public void testCreateUser_Success() throws Exception {
        // Given
        UserCreateRequest request = create_user("test1@example.com", "password123!");

        // When Then
        mockMvc.perform(post("/api/v1/users/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isCreated()) // 201 Created 상태 확인
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.userId").isNumber()) // 응답의 userId가 숫자인지 확인
                .andExpect(jsonPath("$.userId").isNotEmpty()); // 응답의 userId가 비어있지 않은지 확인
    }

    @Test
    @DisplayName("회원가입 실패: 유효하지 않은 이메일 주소")
    public void testCreateUser_Fail_InvalidEmail() throws Exception {
        // Given
        UserCreateRequest request = create_user("invalid-email", "password123!");

        // When Then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(UserErrorCode.INVALID_EMAIL.name()))
                .andExpect(jsonPath("$.message").value(UserErrorCode.INVALID_EMAIL.getMessage()));
    }

    @Test
    @DisplayName("회원가입 실패: 유효하지 않은 비밀번호")
    public void testCreateUser_Fail_InvalidPassword() throws Exception {
        // Given
        UserCreateRequest request = create_user("test2@example.com", "invalid-password");

        // When Then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(UserErrorCode.INVALID_PASSWORD.name()))
                .andExpect(jsonPath("$.message").value(UserErrorCode.INVALID_PASSWORD.getMessage()));
    }

    @Test
    @DisplayName("회원가입 실패: 중복 이메일")
    public void testCreateUser_Fail_DuplicateEmail() throws Exception {
        // Given
        userService.createUser(create_user("test1@example.com", "password123!"));
        UserCreateRequest request = create_user("test1@example.com", "password123!");

        // When Then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(UserErrorCode.DUPLICATED_EMAIL.name()))
                .andExpect(jsonPath("$.message").value(UserErrorCode.DUPLICATED_EMAIL.getMessage()));
    }

    @Test
    @DisplayName("로그인 성공")
    void testLogin_Success() throws Exception {
        // Given
        userService.createUser(create_user("test1@example.com", "password123!"));

        LoginRequest request = LoginRequest.builder()
                .email("test1@example.com")
                .password("password123!")
                .build();

        // When Then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.jwtToken").isString())
                .andExpect(jsonPath("$.username").isString())
                .andExpect(jsonPath("$.email").isString());
    }

    @Test
    @DisplayName("로그인 실패: 존재하지 않는 이메일")
    void testLogin_Fail_EmailNotFound() throws Exception {
        // Given
        userService.createUser(create_user("test1@example.com", "password123!"));

        LoginRequest request = LoginRequest.builder()
                .email("nonexistent@example.com")
                .password("password123!")
                .build();

        // When Then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(jsonPath("$.errorCode").value(UserErrorCode.EMAIL_NOT_FOUND.name()))
                .andExpect(jsonPath("$.message").value(UserErrorCode.EMAIL_NOT_FOUND.getMessage()));
    }

    @Test
    @WithCustomMockUser
    @DisplayName("팔로우 성공")
    void testFollow_Success() throws Exception {
        // Given
        User loginUser = userRepository.save(generateLoginUser());
        User followingUser = userRepository.save(TestDataFactory.generateUserWithId(590L));

        // When Then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users/{followingId}/follow", followingUser.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Following following = followingRepository.findByFollowingUserAndFollowerUser(followingUser, loginUser).orElseThrow();
        assertThat(following.getFollowingUser().getId()).isEqualTo(followingUser.getId());
        assertThat(following.getFollowerUser().getId()).isEqualTo(loginUser.getId());
    }

    @Test
    @WithCustomMockUser
    @DisplayName("언팔로우 성공")
    void testUnfollow_Success() throws Exception {
        // Given
        User loginUser = userRepository.save(generateLoginUser());
        User followingUser = userRepository.save(TestDataFactory.generateUserWithId(590L));
        followingRepository.save(Following.builder()
                .followingUser(followingUser)
                .followerUser(loginUser)
                .build());

        // When Then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users/{followingId}/unfollow", followingUser.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        assertThat(followingRepository.existsByFollowingUserAndFollowerUser(followingUser, loginUser)).isFalse();
    }

    private UserCreateRequest create_user(String email, String password) {
        return UserCreateRequest.builder()
                .bio("")
                .contact("01012345678")
                .email(email)
                .name("Test Name")
                .password(password)
                .profileImage("")
                .build();
    }
}