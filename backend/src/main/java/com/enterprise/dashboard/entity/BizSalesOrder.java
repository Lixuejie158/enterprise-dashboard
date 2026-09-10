package com.enterprise.dashboard.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("biz_sales_order")
public class BizSalesOrder {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String orderNo;
    private Long customerId;
    private LocalDate orderDate;
    private BigDecimal totalAmount;
    private Integer paymentStatus;
    private Long salesPersonId;
    private String region;
    private LocalDateTime createTime;
}
