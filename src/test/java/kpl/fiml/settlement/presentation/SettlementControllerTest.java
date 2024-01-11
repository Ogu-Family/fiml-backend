package kpl.fiml.settlement.presentation;

import kpl.fiml.customMockUser.WithCustomMockUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class SettlementControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    @WithCustomMockUser
    @DisplayName("특정 프로젝트에 대한 정산 정보를 조회할 수 있다.")
    public void testGetSettlementByProject() throws Exception {
        mockMvc.perform(get("/api/v1/settlements/{projectId}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.settleAmount").isNumber())
                .andExpect(jsonPath("$.settledAt").isNotEmpty());
    }
}
