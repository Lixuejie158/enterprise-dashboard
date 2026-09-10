package com.enterprise.dashboard.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class RevenueTrendDTO {
    private String month;
    private BigDecimal revenue;
    private BigDecimal cost;
    private BigDecimal profit;
}
