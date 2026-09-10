package com.enterprise.dashboard.service;

import com.enterprise.dashboard.dto.*;
import java.util.List;

public interface SalesService {
    List<RegionAnalysisDTO> getRegionAnalysis();
    List<ChannelAnalysisDTO> getChannelAnalysis();
    List<TopCustomerDTO> getCustomerRanking(int limit, String region);
    List<ProductSalesDTO> getProductRanking(int limit, String category);
    List<MonthlyComparisonDTO> getMonthlyComparison(String year);
}
