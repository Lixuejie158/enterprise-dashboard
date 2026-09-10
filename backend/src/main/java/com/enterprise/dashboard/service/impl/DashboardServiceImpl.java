package com.enterprise.dashboard.service.impl;

import com.enterprise.dashboard.dto.*;
import com.enterprise.dashboard.mapper.BizFinancialMonthlyMapper;
import com.enterprise.dashboard.mapper.BizOrderItemMapper;
import com.enterprise.dashboard.mapper.BizRiskAlertMapper;
import com.enterprise.dashboard.mapper.BizSalesOrderMapper;
import com.enterprise.dashboard.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private BizSalesOrderMapper salesOrderMapper;

    @Autowired
    private BizFinancialMonthlyMapper financialMapper;

    @Autowired
    private BizOrderItemMapper orderItemMapper;

    @Autowired
    private BizRiskAlertMapper riskAlertMapper;

    @Override
    public DashboardOverviewDTO getOverview() {
        DashboardOverviewDTO dto = new DashboardOverviewDTO();

        BigDecimal totalRevenue = salesOrderMapper.selectTotalRevenue();
        Long totalOrders = salesOrderMapper.selectTotalOrderCount();

        dto.setTotalRevenue(totalRevenue != null ? totalRevenue : BigDecimal.ZERO);
        dto.setTotalOrders(totalOrders != null ? totalOrders : 0L);

        List<Map<String, Object>> financialList = financialMapper.selectMonthlyReport(null);
        if (!financialList.isEmpty()) {
            Map<String, Object> latest = financialList.get(financialList.size() - 1);
            BigDecimal revenue = toBigDecimal(latest.get("revenue"));
            BigDecimal cost = toBigDecimal(latest.get("cost"));
            BigDecimal profit = toBigDecimal(latest.get("grossProfit"));

            dto.setTotalRevenue(revenue);
            dto.setTotalProfit(profit);

            if (revenue.compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal grossMargin = profit.divide(revenue, 4, RoundingMode.HALF_UP)
                        .multiply(new BigDecimal("100"));
                dto.setGrossMargin(grossMargin);
            }
        }

        dto.setTotalCustomers(0L);
        dto.setPendingReceivables(BigDecimal.ZERO);
        dto.setRevenueGrowthRate(BigDecimal.ZERO);
        dto.setProfitGrowthRate(BigDecimal.ZERO);

        return dto;
    }

    @Override
    public List<RevenueTrendDTO> getRevenueTrend(String year) {
        List<Map<String, Object>> dataList = financialMapper.selectRevenueTrend(year);
        List<RevenueTrendDTO> result = new ArrayList<>();

        for (Map<String, Object> data : dataList) {
            RevenueTrendDTO dto = new RevenueTrendDTO();
            dto.setMonth((String) data.get("month"));
            dto.setRevenue(toBigDecimal(data.get("revenue")));
            dto.setCost(toBigDecimal(data.get("cost")));
            dto.setProfit(toBigDecimal(data.get("profit")));
            result.add(dto);
        }

        return result;
    }

    @Override
    public List<SalesByRegionDTO> getSalesByRegion() {
        List<Map<String, Object>> dataList = salesOrderMapper.selectSalesByRegion();
        List<SalesByRegionDTO> result = new ArrayList<>();

        for (Map<String, Object> data : dataList) {
            SalesByRegionDTO dto = new SalesByRegionDTO();
            dto.setRegion((String) data.get("region"));
            dto.setAmount(toBigDecimal(data.get("amount")));
            dto.setOrderCount(toLong(data.get("orderCount")));
            result.add(dto);
        }

        return result;
    }

    @Override
    public List<TopCustomerDTO> getTopCustomers(int limit) {
        List<Map<String, Object>> dataList = salesOrderMapper.selectTopCustomers(limit);
        List<TopCustomerDTO> result = new ArrayList<>();

        for (Map<String, Object> data : dataList) {
            TopCustomerDTO dto = new TopCustomerDTO();
            dto.setCustomerName((String) data.get("customerName"));
            dto.setTotalAmount(toBigDecimal(data.get("totalAmount")));
            dto.setOrderCount(toLong(data.get("orderCount")));
            dto.setRegion((String) data.get("region"));
            result.add(dto);
        }

        return result;
    }

    @Override
    public List<ProductSalesDTO> getProductSales(int limit) {
        List<Map<String, Object>> dataList = orderItemMapper.selectProductSalesRanking(limit);
        List<ProductSalesDTO> result = new ArrayList<>();

        for (Map<String, Object> data : dataList) {
            ProductSalesDTO dto = new ProductSalesDTO();
            dto.setProductName((String) data.get("productName"));
            dto.setCategory((String) data.get("category"));
            dto.setQuantity(toLong(data.get("quantity")));
            dto.setAmount(toBigDecimal(data.get("amount")));
            result.add(dto);
        }

        return result;
    }

    @Override
    public RiskSummaryDTO getRiskSummary() {
        RiskSummaryDTO dto = new RiskSummaryDTO();

        Map<String, Object> summary = riskAlertMapper.selectRiskSummary();
        if (summary != null) {
            dto.setTotal(toLong(summary.get("total")));
            dto.setHigh(toLong(summary.get("high")));
            dto.setMedium(toLong(summary.get("medium")));
            dto.setLow(toLong(summary.get("low")));
        } else {
            dto.setTotal(0L);
            dto.setHigh(0L);
            dto.setMedium(0L);
            dto.setLow(0L);
        }

        List<Map<String, Object>> recentAlertData = riskAlertMapper.selectRecentAlerts(5);
        List<RiskSummaryDTO.RecentAlertDTO> recentAlerts = new ArrayList<>();

        for (Map<String, Object> alert : recentAlertData) {
            RiskSummaryDTO.RecentAlertDTO alertDTO = new RiskSummaryDTO.RecentAlertDTO();
            alertDTO.setTitle((String) alert.get("title"));
            alertDTO.setLevel(toInteger(alert.get("level")));
            alertDTO.setType((String) alert.get("type"));
            alertDTO.setTime(alert.get("time") != null ? alert.get("time").toString() : null);
            recentAlerts.add(alertDTO);
        }

        dto.setRecentAlerts(recentAlerts);

        return dto;
    }

    private BigDecimal toBigDecimal(Object value) {
        if (value == null) {
            return BigDecimal.ZERO;
        }
        if (value instanceof BigDecimal) {
            return (BigDecimal) value;
        }
        if (value instanceof Number) {
            return new BigDecimal(value.toString());
        }
        return BigDecimal.ZERO;
    }

    private Long toLong(Object value) {
        if (value == null) {
            return 0L;
        }
        if (value instanceof Long) {
            return (Long) value;
        }
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        return 0L;
    }

    private Integer toInteger(Object value) {
        if (value == null) {
            return 0;
        }
        if (value instanceof Integer) {
            return (Integer) value;
        }
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        return 0;
    }
}
