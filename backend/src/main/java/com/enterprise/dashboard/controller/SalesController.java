package com.enterprise.dashboard.controller;

import com.enterprise.dashboard.common.Result;
import com.enterprise.dashboard.dto.*;
import com.enterprise.dashboard.service.SalesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/sales")
public class SalesController {
    @Autowired
    private SalesService salesService;

    @GetMapping("/region-analysis")
    public Result<List<RegionAnalysisDTO>> getRegionAnalysis() {
        return Result.success(salesService.getRegionAnalysis());
    }

    @GetMapping("/channel-analysis")
    public Result<List<ChannelAnalysisDTO>> getChannelAnalysis() {
        return Result.success(salesService.getChannelAnalysis());
    }

    @GetMapping("/customer-ranking")
    public Result<List<TopCustomerDTO>> getCustomerRanking(
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(required = false) String region) {
        return Result.success(salesService.getCustomerRanking(limit, region));
    }

    @GetMapping("/product-ranking")
    public Result<List<ProductSalesDTO>> getProductRanking(
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(required = false) String category) {
        return Result.success(salesService.getProductRanking(limit, category));
    }

    @GetMapping("/monthly-comparison")
    public Result<List<MonthlyComparisonDTO>> getMonthlyComparison(@RequestParam(required = false) String year) {
        return Result.success(salesService.getMonthlyComparison(year));
    }
}
