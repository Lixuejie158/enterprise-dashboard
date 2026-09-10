package com.enterprise.dashboard.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class MonthlyReportDTO {
    private String yearMonth;
    private BigDecimal revenue;
    private BigDecimal cost;
    private BigDecimal grossProfit;
    private BigDecimal operatingExpense;
    private BigDecimal netProfit;
    private BigDecimal grossMargin;
    private BigDecimal netMargin;
}
