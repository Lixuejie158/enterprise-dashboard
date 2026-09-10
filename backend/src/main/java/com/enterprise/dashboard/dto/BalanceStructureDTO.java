package com.enterprise.dashboard.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class BalanceStructureDTO {
    private BigDecimal totalAssets;
    private BigDecimal totalLiabilities;
    private BigDecimal ownerEquity;
    private BigDecimal assetsLiabilityRatio;
    private BigDecimal cashFlow;
    private BigDecimal accountsReceivable;
    private BigDecimal accountsPayable;
    private BigDecimal inventoryValue;
}
