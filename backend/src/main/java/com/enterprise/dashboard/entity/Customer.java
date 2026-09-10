package com.enterprise.dashboard.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("crm_customer")
public class Customer {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String customerName;
    private String region;
    private String industry;
    private BigDecimal totalAmount;
    private Long orderCount;
    private String contactPerson;
    private String contactPhone;
    private String status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
