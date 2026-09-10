package com.enterprise.dashboard.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("sys_department")
public class SysDepartment {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String deptName;
    private Long parentId;
    private String leader;
    private Integer sortOrder;
    private Integer status;
    private LocalDateTime createTime;
}
