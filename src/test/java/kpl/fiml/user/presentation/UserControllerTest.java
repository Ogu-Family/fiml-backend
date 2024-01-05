package kpl.fiml.user.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import kpl.fiml.user.domain.UserRepository;
import kpl.fiml.user.dto.request.UserCreateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
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

    @Autowired
    private UserController userController;

    @BeforeEach
    void init() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("회원가입 성공")
    public void testCreateUser_Success() throws Exception {
        // Given
        UserCreateRequest request = UserCreateRequest.builder()
                .bio("")
                .contact("01012345678")
                .email("test1@example.com")
                .name("Test Name")
                .password("password123!")
                .profileImage("")
                .build();

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
        UserCreateRequest request = UserCreateRequest.builder()
                .bio("")
                .contact("01012345678")
                .email("invalid-email")
                .name("Test Name")
                .password("password123!")
                .profileImage("")
                .build();

        // When Then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("INVALID_EMAIL"))
                .andExpect(jsonPath("$.message").value("유효하지 않은 이메일 주소입니다."));
    }

}