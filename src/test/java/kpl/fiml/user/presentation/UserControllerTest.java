package kpl.fiml.user.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import kpl.fiml.user.domain.UserRepository;
import kpl.fiml.user.dto.request.LoginRequest;
import kpl.fiml.user.dto.request.UserCreateRequest;
import kpl.fiml.user.exception.UserErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

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

    @Test
    @DisplayName("회원가입 성공")
    public void testCreateUser_Success() throws Exception {
        // Given
        userRepository.deleteAll();
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
                .andExpect(jsonPath("$.errorCode").value(UserErrorCode.INVALID_EMAIL.getCode()))
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
                .andExpect(jsonPath("$.errorCode").value(UserErrorCode.INVALID_PASSWORD.getCode()))
                .andExpect(jsonPath("$.message").value(UserErrorCode.INVALID_PASSWORD.getMessage()));
    }

    @Test
    @DisplayName("회원가입 실패: 중복 이메일")
    public void testCreateUser_Fail_DuplicateEmail() throws Exception {
        // Given
        UserCreateRequest request = create_user("test1@example.com", "password123!");

        // When Then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(UserErrorCode.DUPLICATED_EMAIL.getCode()))
                .andExpect(jsonPath("$.message").value(UserErrorCode.DUPLICATED_EMAIL.getMessage()));
    }

    @Test
    @DisplayName("로그인 성공")
    void testLogin_Success() throws Exception {
        // Given
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
        LoginRequest request = LoginRequest.builder()
                .email("nonexistent@example.com")
                .password("password123!")
                .build();

        // When Then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(jsonPath("$.errorCode").value(UserErrorCode.EMAIL_NOT_FOUND.getCode()))
                .andExpect(jsonPath("$.message").value(UserErrorCode.EMAIL_NOT_FOUND.getMessage()));
    }
}