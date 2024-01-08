package kpl.fiml.sponsor.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import kpl.fiml.customMockUser.WithCustomMockUser;
import kpl.fiml.sponsor.domain.SponsorStatus;
import kpl.fiml.sponsor.dto.request.SponsorCreateRequest;
import kpl.fiml.sponsor.dto.request.SponsorUpdateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.lang.reflect.Field;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class SponsorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithCustomMockUser
    @DisplayName("후원 정보를 생성할 수 있다.")
    public void testCreateSponsor() throws Exception {
        // given
        SponsorCreateRequest request = new SponsorCreateRequest();
        Field rewardId = request.getClass().getDeclaredField("rewardId");
        rewardId.setAccessible(true);
        rewardId.set(request, 1L);
        Field totalAmount = request.getClass().getDeclaredField("totalAmount");
        totalAmount.setAccessible(true);
        totalAmount.set(request, 60000L);

        // when-then
        mockMvc.perform(post("/api/v1/sponsors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.id").isNotEmpty());
    }

    @Test
    @WithCustomMockUser
    @DisplayName("특정 회원의 후원 리스트를 조회할 수 있다.")
    public void testGetSponsorsByUser() throws Exception {
        mockMvc.perform(get("/api/v1/sponsors"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].userId").value(1))
                .andExpect(jsonPath("$.[0].rewardId").value(1))
                .andExpect(jsonPath("$.[0].totalAmount").value(60000))
                .andExpect(jsonPath("$.[0].sponsorStatus").value(SponsorStatus.FUNDING_PROCEEDING.getDisplayName()));
    }

    @Test
    @WithCustomMockUser
    @DisplayName("특정 프로젝트의 후원 리스트를 조회할 수 있다.")
    public void testGetSponsorsByProject() throws Exception {
        mockMvc.perform(get("/api/v1/sponsors/project/{projectId}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].userId").value(1))
                .andExpect(jsonPath("$.[0].rewardId").value(1))
                .andExpect(jsonPath("$.[0].totalAmount").value(60000))
                .andExpect(jsonPath("$.[0].sponsorStatus").value(SponsorStatus.FUNDING_PROCEEDING.getDisplayName()));
    }

    @Test
    @WithCustomMockUser
    @DisplayName("후원 정보를 수정할 수 있다.")
    public void testUpdateSponsor() throws Exception {
        // given
        SponsorUpdateRequest request = new SponsorUpdateRequest();
        Field rewardId = request.getClass().getDeclaredField("rewardId");
        rewardId.setAccessible(true);
        rewardId.set(request, 1L);
        Field totalAmount = request.getClass().getDeclaredField("totalAmount");
        totalAmount.setAccessible(true);
        totalAmount.set(request, 70000L);

        // when-then
        mockMvc.perform(patch("/api/v1/sponsors/{sponsorId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.rewardId").value(1))
                .andExpect(jsonPath("$.totalAmount").value(70000))
                .andExpect(jsonPath("$.sponsorStatus").value(SponsorStatus.FUNDING_PROCEEDING.getDisplayName()));
    }

    @Test
    @WithCustomMockUser
    @DisplayName("후원을 취소할 수 있다.")
    public void testDeleteSponsor() throws Exception {
        mockMvc.perform(delete("/api/v1/sponsors/{sponsorId}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.deleteAt").isNotEmpty());
    }
}
