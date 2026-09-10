package com.enterprise.dashboard.service;

import com.enterprise.dashboard.dto.RiskStatisticsDTO;
import com.enterprise.dashboard.entity.BizRiskAlert;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

public interface RiskService {
    Page<BizRiskAlert> getRiskList(Integer status, Integer level, String type, int page, int size);
    RiskStatisticsDTO getRiskStatistics();
}
