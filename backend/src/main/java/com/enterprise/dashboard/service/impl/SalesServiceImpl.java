package com.enterprise.dashboard.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.enterprise.dashboard.dto.*;
import com.enterprise.dashboard.entity.BizCustomer;
import com.enterprise.dashboard.entity.BizOrderItem;
import com.enterprise.dashboard.entity.BizProduct;
import com.enterprise.dashboard.entity.BizSalesOrder;
import com.enterprise.dashboard.mapper.BizCustomerMapper;
import com.enterprise.dashboard.mapper.BizOrderItemMapper;
import com.enterprise.dashboard.mapper.BizProductMapper;
import com.enterprise.dashboard.mapper.BizSalesOrderMapper;
import com.enterprise.dashboard.service.SalesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SalesServiceImpl implements SalesService {

    @Autowired
    private BizSalesOrderMapper salesOrderMapper;

    @Autowired
    private BizCustomerMapper customerMapper;

    @Autowired
    private BizProductMapper productMapper;

    @Autowired
    private BizOrderItemMapper orderItemMapper;

    @Override
    public List<RegionAnalysisDTO> getRegionAnalysis() {
        List<BizSalesOrder> orders = salesOrderMapper.selectList(null);
        Map<String, List<BizSalesOrder>> regionGroups = orders.stream()
                .collect(Collectors.groupingBy(order -> order.getRegion() != null ? order.getRegion() : "Unknown"));

        List<RegionAnalysisDTO> result = new ArrayList<>();

        for (Map.Entry<String, List<BizSalesOrder>> entry : regionGroups.entrySet()) {
            RegionAnalysisDTO dto = new RegionAnalysisDTO();
            dto.setRegion(entry.getKey());

            List<BizSalesOrder> regionOrders = entry.getValue();
            BigDecimal totalAmount = regionOrders.stream()
                    .map(BizSalesOrder::getTotalAmount)
                    .filter(amount -> amount != null)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            long orderCount = regionOrders.size();
            long customerCount = regionOrders.stream()
                    .map(BizSalesOrder::getCustomerId)
                    .distinct()
                    .count();

            dto.setTotalAmount(totalAmount);
            dto.setOrderCount(orderCount);
            dto.setCustomerCount(customerCount);

            if (orderCount > 0) {
                BigDecimal avgOrderAmount = totalAmount.divide(new BigDecimal(orderCount), 4, RoundingMode.HALF_UP);
                dto.setAvgOrderAmount(avgOrderAmount);
            }

            dto.setGrowthRate(BigDecimal.ZERO);

            result.add(dto);
        }

        return result;
    }

    @Override
    public List<ChannelAnalysisDTO> getChannelAnalysis() {
        List<BizSalesOrder> orders = salesOrderMapper.selectList(null);

        Map<String, BigDecimal> channelAmounts = new HashMap<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (BizSalesOrder order : orders) {
            String channel = order.getRegion() != null ? order.getRegion() : "Unknown";
            BigDecimal amount = order.getTotalAmount() != null ? order.getTotalAmount() : BigDecimal.ZERO;
            channelAmounts.merge(channel, amount, BigDecimal::add);
            totalAmount = totalAmount.add(amount);
        }

        List<ChannelAnalysisDTO> result = new ArrayList<>();

        for (Map.Entry<String, BigDecimal> entry : channelAmounts.entrySet()) {
            ChannelAnalysisDTO dto = new ChannelAnalysisDTO();
            dto.setChannel(entry.getKey());
            dto.setAmount(entry.getValue());

            if (totalAmount.compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal percentage = entry.getValue()
                        .divide(totalAmount, 4, RoundingMode.HALF_UP)
                        .multiply(new BigDecimal("100"));
                dto.setPercentage(percentage);
            }

            result.add(dto);
        }

        return result;
    }

    @Override
    public List<TopCustomerDTO> getCustomerRanking(int limit, String region) {
        LambdaQueryWrapper<BizSalesOrder> wrapper = new LambdaQueryWrapper<>();

        if (region != null && !region.isEmpty()) {
            wrapper.eq(BizSalesOrder::getRegion, region);
        }

        List<BizSalesOrder> orders = salesOrderMapper.selectList(wrapper);

        Map<Long, List<BizSalesOrder>> customerGroups = orders.stream()
                .collect(Collectors.groupingBy(BizSalesOrder::getCustomerId));

        List<TopCustomerDTO> customerRankings = new ArrayList<>();

        for (Map.Entry<Long, List<BizSalesOrder>> entry : customerGroups.entrySet()) {
            List<BizSalesOrder> customerOrders = entry.getValue();

            if (!customerOrders.isEmpty()) {
                BizSalesOrder firstOrder = customerOrders.get(0);

                TopCustomerDTO dto = new TopCustomerDTO();
                dto.setCustomerName(firstOrder.getCustomerName());
                dto.setRegion(firstOrder.getRegion());

                BigDecimal totalAmount = customerOrders.stream()
                        .map(BizSalesOrder::getTotalAmount)
                        .filter(amount -> amount != null)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                dto.setTotalAmount(totalAmount);
                dto.setOrderCount((long) customerOrders.size());

                customerRankings.add(dto);
            }
        }

        customerRankings.sort((a, b) -> b.getTotalAmount().compareTo(a.getTotalAmount()));

        if (customerRankings.size() > limit) {
            return customerRankings.subList(0, limit);
        }

        return customerRankings;
    }

    @Override
    public List<ProductSalesDTO> getProductRanking(int limit, String category) {
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
    public List<MonthlyComparisonDTO> getMonthlyComparison(String year) {
        List<BizSalesOrder> orders = salesOrderMapper.selectList(null);

        Map<String, List<BizSalesOrder>> monthGroups = orders.stream()
                .collect(Collectors.groupingBy(order -> {
                    if (order.getOrderDate() != null) {
                        return String.format("%04d-%02d", order.getOrderDate().getYear(), order.getOrderDate().getMonthValue());
                    }
                    return "Unknown";
                }));

        List<MonthlyComparisonDTO> result = new ArrayList<>();

        for (Map.Entry<String, List<BizSalesOrder>> entry : monthGroups.entrySet()) {
            MonthlyComparisonDTO dto = new MonthlyComparisonDTO();
            dto.setMonth(entry.getKey());

            BigDecimal currentYearAmount = entry.getValue().stream()
                    .map(BizSalesOrder::getTotalAmount)
                    .filter(amount -> amount != null)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            dto.setCurrentYear(currentYearAmount);
            dto.setLastYear(BigDecimal.ZERO);
            dto.setGrowthRate(BigDecimal.ZERO);

            result.add(dto);
        }

        result.sort((a, b) -> a.getMonth().compareTo(b.getMonth()));

        return result;
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
}
