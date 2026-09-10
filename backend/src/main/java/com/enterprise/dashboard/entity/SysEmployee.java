package com.enterprise.dashboard.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("sys_employee")
public class SysEmployee {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String empName;
    private Long deptId;
    private String position;
    private LocalDate hireDate;
    private Integer status;
    private LocalDateTime createTime;
}
