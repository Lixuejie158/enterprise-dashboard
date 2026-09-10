package com.enterprise.dashboard.controller;

import com.enterprise.dashboard.common.Result;
import com.enterprise.dashboard.dto.*;
import com.enterprise.dashboard.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {
    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/overview")
    public Result<DashboardOverviewDTO> getOverview() {
        return Result.success(dashboardService.getOverview());
    }

    @GetMapping("/revenue-trend")
    public Result<List<RevenueTrendDTO>> getRevenueTrend(@RequestParam(required = false) String year) {
        return Result.success(dashboardService.getRevenueTrend(year));
    }

    @GetMapping("/sales-by-region")
    public Result<List<SalesByRegionDTO>> getSalesByRegion() {
        return Result.success(dashboardService.getSalesByRegion());
    }

    @GetMapping("/top-customers")
    public Result<List<TopCustomerDTO>> getTopCustomers(@RequestParam(defaultValue = "10") int limit) {
        return Result.success(dashboardService.getTopCustomers(limit));
    }

    @GetMapping("/product-sales")
    public Result<List<ProductSalesDTO>> getProductSales(@RequestParam(defaultValue = "10") int limit) {
        return Result.success(dashboardService.getProductSales(limit));
    }

    @GetMapping("/risk-summary")
    public Result<RiskSummaryDTO> getRiskSummary() {
        return Result.success(dashboardService.getRiskSummary());
    }
}
