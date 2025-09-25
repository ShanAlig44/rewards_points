package com.rewards.components;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.rewards.model.Transaction;
import com.rewards.repository.TransactionRepository;

@Component
public class DataLoader implements CommandLineRunner {

    private final TransactionRepository  transactionRepository;

    public DataLoader(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public void run(String... args) {
    	transactionRepository.saveAll(Arrays.asList(
                new Transaction("Alice", BigDecimal.valueOf(120.0), "2025-01"),
                new Transaction("Alice", BigDecimal.valueOf(75.0), "2025-02"),
                new Transaction("Alice", BigDecimal.valueOf(200.0), "2025-03"),

                new Transaction("Bob", BigDecimal.valueOf(45.0), "2025-01"),
                new Transaction("Bob", BigDecimal.valueOf(110.0), "2025-02"),
                new Transaction("Bob", BigDecimal.valueOf(60.0), "2025-03"),

                new Transaction("Charlie",BigDecimal.valueOf( 130.0), "2025-01"),
                new Transaction("Charlie", BigDecimal.valueOf(95.0), "2025-02"),
                new Transaction("Charlie", BigDecimal.valueOf(55.0), "2025-06")
        ));
    	
    	
    	
    }
}
