package com.enterprise.dashboard.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class TopCustomerDTO {
    private String customerName;
    private BigDecimal totalAmount;
    private Long orderCount;
    private String region;
}
