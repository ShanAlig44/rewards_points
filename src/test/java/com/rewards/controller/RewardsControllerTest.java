package com.rewards.controller;

import com.rewards.RewardsApplication;
import com.rewards.dto.CustomerRewards;
import com.rewards.model.Transaction;
import com.rewards.service.RewardsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = RewardsController.class)
@Import(RewardsApplication.class) 
class RewardsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RewardsService rewardsService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetRewards() throws Exception {
        List<Transaction> mockTransactions = new ArrayList<>();
        mockTransactions.add(new Transaction("John", BigDecimal.valueOf(120.0), "2025-01"));
        mockTransactions.add(new Transaction("John", BigDecimal.valueOf(100.0), "2025-02"));
        mockTransactions.add(new Transaction("Jane", BigDecimal.valueOf(80.0), "2025-03"));

       
        List<CustomerRewards> mockRewards = new ArrayList<>();
        mockRewards.add(new CustomerRewards("John", null, 135, BigDecimal.valueOf(220.0)));
        mockRewards.add(new CustomerRewards("Jane", null, 30, BigDecimal.valueOf(80.0)));

        when(rewardsService.sampleTransactions()).thenReturn(mockTransactions);
        when(rewardsService.calculateRewards(mockTransactions)).thenReturn(mockRewards);

        mockMvc.perform(get("/api/rewards"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].customerName").value("John"))
                .andExpect(jsonPath("$[0].totalPoints").value(135))
                .andExpect(jsonPath("$[1].customerName").value("Jane"))
                .andExpect(jsonPath("$[1].totalPoints").value(30));
    }

}
