package com.rewards.service;

import com.rewards.dto.CustomerRewards;
import com.rewards.dto.MonthlyRewards;
import com.rewards.exception.RewardsCalculationException;
import com.rewards.model.Transaction;
import com.rewards.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class RewardsServiceTest {

    private TransactionRepository transactionRepository;
    private RewardsService rewardsService;

    @BeforeEach
    void setUp() {
        transactionRepository = Mockito.mock(TransactionRepository.class);
        rewardsService = new RewardsService(transactionRepository);
    }

    @Test
    void testCalculateRewards() {
        Transaction t1 = new Transaction("John", BigDecimal.valueOf(120), "2025-01");
        Transaction t2 = new Transaction("John", BigDecimal.valueOf(80), "2025-02");
        Transaction t3 = new Transaction("Jane", BigDecimal.valueOf(75), "2025-03");

        List<Transaction> transactions = Arrays.asList(t1, t2, t3);

        List<CustomerRewards> rewards = rewardsService.calculateRewards(transactions);

        assertEquals(2, rewards.size());

        CustomerRewards johnRewards = rewards.get(0);
        assertEquals("Jane".compareTo("John") < 0 ? "Jane" : "John", johnRewards.getCustomerName()); // sorted alphabetically
        assertTrue(johnRewards.getTotalPoints() > 0);
        assertTrue(johnRewards.getTotalAmount().compareTo(BigDecimal.ZERO) > 0);
        assertNotNull(johnRewards.getTotalPoints());
        assertFalse(johnRewards.getMonthlyPoints().isEmpty());

        CustomerRewards janeRewards = rewards.get(1);
        assertEquals("Jane".compareTo("John") < 0 ? "John" : "Jane", janeRewards.getCustomerName());
        assertTrue(janeRewards.getTotalPoints() > 0);
        assertTrue(janeRewards.getTotalAmount().compareTo(BigDecimal.ZERO) > 0);
        assertNotNull(janeRewards.getTotalPoints());
    }

    @Test
    void testCalculateRewardsEmptyList() {
        List<Transaction> emptyList = List.of();
        RewardsCalculationException exception = assertThrows(RewardsCalculationException.class,
                () -> rewardsService.calculateRewards(emptyList));
        assertEquals("No transactions provided", exception.getMessage());
    }

    @Test
    void testSampleTransactions() {
        Transaction t1 = new Transaction("John", BigDecimal.valueOf(100), "2025-01");
        when(transactionRepository.findAll()).thenReturn(List.of(t1));

        List<Transaction> transactions = rewardsService.sampleTransactions();
        assertEquals(1, transactions.size());
        assertEquals("John", transactions.get(0).getCustomerName());
    }
}
