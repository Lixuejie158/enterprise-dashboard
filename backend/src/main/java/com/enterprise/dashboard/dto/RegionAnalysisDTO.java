package com.enterprise.dashboard.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class RegionAnalysisDTO {
    private String region;
    private BigDecimal totalAmount;
    private Long orderCount;
    private Long customerCount;
    private BigDecimal avgOrderAmount;
    private BigDecimal growthRate;
}
