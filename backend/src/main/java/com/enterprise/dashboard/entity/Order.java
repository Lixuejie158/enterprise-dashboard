package com.enterprise.dashboard.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("ord_order")
public class Order {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String orderNo;
    private Long customerId;
    private String customerName;
    private String region;
    private String channel;
    private BigDecimal amount;
    private BigDecimal cost;
    private BigDecimal profit;
    private String status;
    private LocalDateTime orderTime;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
