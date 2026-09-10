package com.enterprise.dashboard.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ChannelAnalysisDTO {
    private String channel;
    private BigDecimal amount;
    private BigDecimal percentage;
}
