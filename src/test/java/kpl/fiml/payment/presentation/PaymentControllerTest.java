package kpl.fiml.payment.presentation;

import kpl.fiml.customMockUser.WithCustomMockUser;
import kpl.fiml.payment.domain.PaymentStatus;
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
public class PaymentControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    @WithCustomMockUser
    @DisplayName("결제 정보를 조회할 수 있다.")
    public void testGetPaymentsOfSuccessAndFail() throws Exception {
        mockMvc.perform(get("/api/v1/payments/{sponsorId}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].paymentStatus").value(PaymentStatus.FAIL.getDisplayName()))
                .andExpect(jsonPath("$.[1].paymentStatus").value(PaymentStatus.SUCCESS.getDisplayName()));
    }
}
