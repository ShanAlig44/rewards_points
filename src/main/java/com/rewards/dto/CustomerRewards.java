package com.rewards.dto;

import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data 
@AllArgsConstructor
public class CustomerRewards {
    private String customerName;
    private List<MonthlyRewards> monthlyPoints;
    private long totalPoints;
    private BigDecimal totalAmount;

    public CustomerRewards() {}
}
