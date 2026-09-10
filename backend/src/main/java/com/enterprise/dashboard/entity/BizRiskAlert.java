package com.enterprise.dashboard.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("biz_risk_alert")
public class BizRiskAlert {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String alertType;
    private Integer alertLevel;
    private String title;
    private String content;
    private Long deptId;
    private Integer status;
    private LocalDateTime alertTime;
    private LocalDateTime resolveTime;
}
