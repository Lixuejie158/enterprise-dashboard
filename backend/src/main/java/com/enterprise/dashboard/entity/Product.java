package com.enterprise.dashboard.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("pms_product")
public class Product {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String productName;
    private String category;
    private BigDecimal price;
    private BigDecimal cost;
    private Long stock;
    private String status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
