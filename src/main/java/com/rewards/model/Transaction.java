package com.rewards.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Data 
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customerName;
    private BigDecimal amount;
    private String transactionMonth;
   
    Transaction(){};
    public Transaction(String customerName, BigDecimal amount, String transactionMonth) {
        this.customerName = customerName;
        this.amount = amount;
        this.transactionMonth = transactionMonth;
    }
  
}

