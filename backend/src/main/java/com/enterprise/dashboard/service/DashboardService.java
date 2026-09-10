package com.enterprise.dashboard.service;

import com.enterprise.dashboard.dto.*;
import java.util.List;

public interface DashboardService {
    DashboardOverviewDTO getOverview();
    List<RevenueTrendDTO> getRevenueTrend(String year);
    List<SalesByRegionDTO> getSalesByRegion();
    List<TopCustomerDTO> getTopCustomers(int limit);
    List<ProductSalesDTO> getProductSales(int limit);
    RiskSummaryDTO getRiskSummary();
}
