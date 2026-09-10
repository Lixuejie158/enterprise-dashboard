package com.enterprise.dashboard.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.enterprise.dashboard.dto.RiskStatisticsDTO;
import com.enterprise.dashboard.entity.BizRiskAlert;
import com.enterprise.dashboard.mapper.BizRiskAlertMapper;
import com.enterprise.dashboard.service.RiskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class RiskServiceImpl implements RiskService {

    @Autowired
    private BizRiskAlertMapper riskAlertMapper;

    @Override
    public Page<BizRiskAlert> getRiskList(Integer status, Integer level, String type, int page, int size) {
        LambdaQueryWrapper<BizRiskAlert> wrapper = new LambdaQueryWrapper<>();

        if (status != null) {
            wrapper.eq(BizRiskAlert::getStatus, status);
        }
        if (level != null) {
            wrapper.eq(BizRiskAlert::getAlertLevel, level);
        }
        if (type != null && !type.isEmpty()) {
            wrapper.eq(BizRiskAlert::getAlertType, type);
        }

        wrapper.orderByDesc(BizRiskAlert::getAlertTime);

        Page<BizRiskAlert> result = riskAlertMapper.selectPage(new Page<>(page, size), wrapper);

        return result;
    }

    @Override
    public RiskStatisticsDTO getRiskStatistics() {
        RiskStatisticsDTO dto = new RiskStatisticsDTO();

        List<Map<String, Object>> typeData = riskAlertMapper.selectRiskStatisticsByType();
        List<RiskStatisticsDTO.TypeCount> byType = new ArrayList<>();

        for (Map<String, Object> data : typeData) {
            RiskStatisticsDTO.TypeCount typeCount = new RiskStatisticsDTO.TypeCount();
            typeCount.setType((String) data.get("type"));
            typeCount.setCount(toLong(data.get("count")));
            byType.add(typeCount);
        }
        dto.setByType(byType);

        List<Map<String, Object>> levelData = riskAlertMapper.selectRiskStatisticsByLevel();
        List<RiskStatisticsDTO.LevelCount> byLevel = new ArrayList<>();

        for (Map<String, Object> data : levelData) {
            RiskStatisticsDTO.LevelCount levelCount = new RiskStatisticsDTO.LevelCount();
            levelCount.setLevel(toInteger(data.get("level")));
            levelCount.setCount(toLong(data.get("count")));
            byLevel.add(levelCount);
        }
        dto.setByLevel(byLevel);

        List<Map<String, Object>> trendData = riskAlertMapper.selectRiskTrend();
        List<RiskStatisticsDTO.MonthCount> trend = new ArrayList<>();

        for (Map<String, Object> data : trendData) {
            RiskStatisticsDTO.MonthCount monthCount = new RiskStatisticsDTO.MonthCount();
            monthCount.setMonth((String) data.get("month"));
            monthCount.setCount(toLong(data.get("count")));
            trend.add(monthCount);
        }
        dto.setTrend(trend);

        return dto;
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
