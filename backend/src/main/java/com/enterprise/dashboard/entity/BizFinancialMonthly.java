package com.enterprise.dashboard.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("biz_financial_monthly")
public class BizFinancialMonthly {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String yearMonth;
    private Long deptId;
    private BigDecimal revenue;
    private BigDecimal cost;
    private BigDecimal grossProfit;
    private BigDecimal operatingExpense;
    private BigDecimal netProfit;
    private BigDecimal totalAssets;
    private BigDecimal totalLiabilities;
    private BigDecimal cashFlow;
    private BigDecimal accountsReceivable;
    private BigDecimal accountsPayable;
    private BigDecimal inventoryValue;
    private LocalDateTime createTime;
}
