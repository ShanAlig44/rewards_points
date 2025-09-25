package com.rewards.dto;


import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data 
@AllArgsConstructor
public class MonthlyRewards {
    private String month; 
    private long points;
    private BigDecimal amount;
    public MonthlyRewards() {}

}
