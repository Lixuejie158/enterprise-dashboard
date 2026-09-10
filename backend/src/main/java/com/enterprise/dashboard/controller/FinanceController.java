package com.enterprise.dashboard.controller;

import com.enterprise.dashboard.common.Result;
import com.enterprise.dashboard.dto.*;
import com.enterprise.dashboard.service.FinanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/finance")
public class FinanceController {
    @Autowired
    private FinanceService financeService;

    @GetMapping("/monthly-report")
    public Result<List<MonthlyReportDTO>> getMonthlyReport(@RequestParam(required = false) String year) {
        return Result.success(financeService.getMonthlyReport(year));
    }

    @GetMapping("/balance-structure")
    public Result<BalanceStructureDTO> getBalanceStructure(@RequestParam(required = false) String yearMonth) {
        return Result.success(financeService.getBalanceStructure(yearMonth));
    }

    @GetMapping("/cost-analysis")
    public Result<List<CostAnalysisDTO>> getCostAnalysis(@RequestParam(required = false) String year) {
        return Result.success(financeService.getCostAnalysis(year));
    }
}
