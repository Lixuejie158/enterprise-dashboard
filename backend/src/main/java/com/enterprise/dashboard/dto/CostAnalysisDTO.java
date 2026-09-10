package com.enterprise.dashboard.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class CostAnalysisDTO {
    private String month;
    private BigDecimal productionCost;
    private BigDecimal operatingExpense;
    private BigDecimal otherCost;
}
