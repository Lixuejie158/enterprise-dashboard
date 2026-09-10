package com.enterprise.dashboard.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class SalesByRegionDTO {
    private String region;
    private BigDecimal amount;
    private Long orderCount;
}
