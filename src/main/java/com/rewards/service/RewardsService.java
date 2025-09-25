package com.rewards.service;

import com.rewards.dto.CustomerRewards;
import com.rewards.dto.MonthlyRewards;
import com.rewards.exception.RewardsCalculationException;
import com.rewards.model.Transaction;
import com.rewards.repository.TransactionRepository;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RewardsService {


	private final TransactionRepository transactionRepository;
	public RewardsService(TransactionRepository transactionRepository) {
		this.transactionRepository = transactionRepository;
	}


	public List<CustomerRewards> calculateRewards(List<Transaction> transactions) {	

		Map<String, Map<String, Long>> rewardPointsByCustomer = getRewardPointsByCustomer(transactions);

		if (rewardPointsByCustomer == null || rewardPointsByCustomer.isEmpty()) {
			log.error("Reward Points By Customer list is null or empty");
			throw new RewardsCalculationException("Reward Points By Customer list cannot be null or empty");
		}

		return rewardPointsByCustomer.entrySet().stream()
				.map(entry -> {
					String customerName = entry.getKey();
					Map<String, Long> monthMap = entry.getValue();

					List<MonthlyRewards> monthlyPoints = getMonthlyPoints(monthMap,customerName,transactions);

					long totalPoints = monthlyPoints.stream()
							.mapToLong(MonthlyRewards::getPoints)
							.sum();

					BigDecimal totalAmount = getTotalAmount(transactions, customerName);

					CustomerRewards customerRewards = new CustomerRewards();
					customerRewards.setCustomerName(customerName);
					customerRewards.setMonthlyPoints(monthlyPoints);
					customerRewards.setTotalPoints(totalPoints);
					customerRewards.setTotalAmount(totalAmount);

					log.debug("Customer Rewards: {}", customerRewards);

					return customerRewards;
				})
				.toList();
	}

	private List<MonthlyRewards> getMonthlyPoints(Map<String, Long> v, String cutomerName,List<Transaction> transactions) {
		return	v.entrySet().stream()
				.map(e -> new MonthlyRewards(e.getKey(), e.getValue(), getEachTransactionAmount(e.getKey(),cutomerName,transactions)))
				.sorted((a, b) -> a.getMonth().compareTo(b.getMonth()))
				.toList();

	}
	
	private BigDecimal getEachTransactionAmount(String month, String customerName,List<Transaction> transactions) {
		return transactions.stream()
	            .filter(tx -> tx.getCustomerName().equals(customerName) && tx.getTransactionMonth().equals(month))
	            .map(Transaction::getAmount) 
	            .reduce(BigDecimal.ZERO, BigDecimal::add);
		
	}

	private BigDecimal getTotalAmount(List<Transaction> transactions, String customerName) {
		return transactions.stream()
				.filter(t -> t.getCustomerName().equals(customerName))
				.map(Transaction::getAmount)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
	}


	private long calculatePoints(BigDecimal amount) {
		long points = 0;
		if (amount.compareTo(BigDecimal.valueOf(100)) > 0) {
			points += 2 * (amount.subtract(BigDecimal.valueOf(100)).longValue());
			points += 50; 
		} else if (amount.compareTo(BigDecimal.valueOf(50)) > 0) {
			points += amount.subtract(BigDecimal.valueOf(50)).longValue();
		}
		return points;
	}



	private Map<String, Map<String, Long>> getRewardPointsByCustomer(List<Transaction> transactions) {
		if (transactions == null || transactions.isEmpty()) {
			log.error("Transaction list is null or empty");
			throw new RewardsCalculationException("Transaction list cannot be null or empty");
		}

		Map<String, Map<String, BigDecimal>> amountByCustomer = transactions.stream()
				.collect(Collectors.groupingBy(Transaction::getCustomerName,
						Collectors.groupingBy(Transaction::getTransactionMonth,
								Collectors.reducing(BigDecimal.ZERO,
										t -> Optional.ofNullable(t.getAmount()).orElse(BigDecimal.ZERO),
										BigDecimal::add))));

		log.info("Amount by customer: {}", amountByCustomer);

		return amountByCustomer.entrySet().stream()
				.collect(Collectors.toMap(
						Map.Entry::getKey, 
						e -> e.getValue().entrySet().stream()
						.collect(Collectors.toMap(
								Map.Entry::getKey, 
								monthEntry -> calculatePoints(monthEntry.getValue())
								))
						));
	}



	public List<Transaction> sampleTransactions() {
		return transactionRepository.findAll();
	}


}


