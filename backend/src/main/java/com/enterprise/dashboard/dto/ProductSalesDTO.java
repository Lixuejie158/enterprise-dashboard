package com.enterprise.dashboard.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductSalesDTO {
    private String productName;
    private String category;
    private Long quantity;
    private BigDecimal amount;
}
