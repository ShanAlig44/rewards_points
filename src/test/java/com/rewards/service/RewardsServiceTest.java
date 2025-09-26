package com.rewards.service;

import com.rewards.dto.CustomerRewards;
import com.rewards.dto.MonthlyRewards;
import com.rewards.exception.RewardsCalculationException;
import com.rewards.model.Transaction;
import com.rewards.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RewardsServiceTest {

	private TransactionRepository transactionRepository;
	private RewardsService rewardsService;

	@BeforeEach
	void setUp() {
		transactionRepository = mock(TransactionRepository.class);
		rewardsService = new RewardsService(transactionRepository);
	}

	@Test
	void testCalculateRewards_success_multipleCustomers() {
		// given
		List<Transaction> transactions = new ArrayList<>();
		transactions.add(new Transaction("Alice", BigDecimal.valueOf(120), "2025-08"));
		transactions.add(new Transaction("Alice", BigDecimal.valueOf(80), "2025-09"));
		transactions.add(new Transaction("Bob", BigDecimal.valueOf(200), "2025-08"));

		// when
		List<CustomerRewards> result = rewardsService.calculateRewards(transactions);

		// then
		assertNotNull(result);
		assertEquals(2, result.size());

		CustomerRewards alice = result.stream().filter(c -> c.getCustomerName().equals("Alice")).findFirst()
				.orElseThrow();

		assertEquals("Alice", alice.getCustomerName());
		assertEquals(BigDecimal.valueOf(200), alice.getTotalAmount());
		assertTrue(alice.getTotalPoints() > 0);

		List<MonthlyRewards> aliceMonths = alice.getMonthlyPoints();
		assertEquals(2, aliceMonths.size());
		assertEquals("2025-08", aliceMonths.get(0).getMonth());
		assertEquals("2025-09", aliceMonths.get(1).getMonth());
	}

	@Test
	void testCalculateRewards_emptyTransactions_throwsException() {
		List<Transaction> transactions = List.of();

		RewardsCalculationException ex = assertThrows(RewardsCalculationException.class,
				() -> rewardsService.calculateRewards(transactions));

		assertEquals("Transaction list cannot be null or empty", ex.getMessage());
	}

	@Test
	void testSampleTransactions_repositoryDelegation() {
		when(transactionRepository.findAll())
				.thenReturn(Arrays.asList(new Transaction("Charlie", BigDecimal.valueOf(150), "2025-09")));

		List<Transaction> result = rewardsService.sampleTransactions();

		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals("Charlie", result.get(0).getCustomerName());
		verify(transactionRepository, times(1)).findAll();
	}
}
