package com.enterprise.dashboard.controller;

import com.enterprise.dashboard.common.Result;
import com.enterprise.dashboard.dto.RiskStatisticsDTO;
import com.enterprise.dashboard.entity.BizRiskAlert;
import com.enterprise.dashboard.service.RiskService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/risk")
public class RiskController {
    @Autowired
    private RiskService riskService;

    @GetMapping("/list")
    public Result<Page<BizRiskAlert>> getRiskList(
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Integer level,
            @RequestParam(required = false) String type,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return Result.success(riskService.getRiskList(status, level, type, page, size));
    }

    @GetMapping("/statistics")
    public Result<RiskStatisticsDTO> getRiskStatistics() {
        return Result.success(riskService.getRiskStatistics());
    }
}
