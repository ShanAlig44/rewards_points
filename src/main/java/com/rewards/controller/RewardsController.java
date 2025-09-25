package com.rewards.controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.rewards.dto.CustomerRewards;
import com.rewards.exception.RewardsCalculationException;
import com.rewards.model.Transaction;
import com.rewards.service.RewardsService;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
public class RewardsController {

	private final RewardsService rewardsService;
    public RewardsController(RewardsService rewardsService) {
        this.rewardsService = rewardsService;
    }

    @GetMapping("/api/rewards")
    public List<CustomerRewards> getRewards() {
        List<Transaction> transactions = rewardsService.sampleTransactions();

        if (transactions == null || transactions.isEmpty()) {
            log.error("Transactions list is null or empty");
            throw new RewardsCalculationException("Transactions must not be null or empty");
        }

        log.info("Fetched {} transactions", transactions.size());
        log.debug("Transaction list details: {}", transactions);

        return rewardsService.calculateRewards(transactions);
    }

}
