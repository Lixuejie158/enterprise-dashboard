package com.enterprise.dashboard.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class MonthlyComparisonDTO {
    private String month;
    private BigDecimal currentYear;
    private BigDecimal lastYear;
    private BigDecimal growthRate;
}
