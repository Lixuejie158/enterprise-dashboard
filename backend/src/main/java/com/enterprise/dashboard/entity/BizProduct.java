package com.enterprise.dashboard.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("biz_product")
public class BizProduct {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String productName;
    private String category;
    private BigDecimal unitPrice;
    private String unit;
    private Integer status;
    private LocalDateTime createTime;
}
