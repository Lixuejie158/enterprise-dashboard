package com.enterprise.dashboard.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("biz_customer")
public class BizCustomer {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String customerName;
    private String region;
    private String industry;
    private String level;
    private String contactPhone;
    private LocalDateTime createTime;
}
