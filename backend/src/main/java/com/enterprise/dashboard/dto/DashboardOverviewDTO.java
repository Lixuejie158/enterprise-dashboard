package com.enterprise.dashboard.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class DashboardOverviewDTO {
    private BigDecimal totalRevenue;
    private BigDecimal totalProfit;
    private BigDecimal revenueGrowthRate;
    private BigDecimal profitGrowthRate;
    private BigDecimal grossMargin;
    private Long totalOrders;
    private Long totalCustomers;
    private BigDecimal pendingReceivables;
}
