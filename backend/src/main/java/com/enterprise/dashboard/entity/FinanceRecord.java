package com.enterprise.dashboard.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("fin_record")
public class FinanceRecord {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String yearMonth;
    private String recordType;
    private BigDecimal revenue;
    private BigDecimal cost;
    private BigDecimal grossProfit;
    private BigDecimal operatingExpense;
    private BigDecimal netProfit;
    private BigDecimal totalAssets;
    private BigDecimal totalLiabilities;
    private BigDecimal ownerEquity;
    private BigDecimal cashFlow;
    private BigDecimal accountsReceivable;
    private BigDecimal accountsPayable;
    private BigDecimal inventoryValue;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
