package com.enterprise.dashboard.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.enterprise.dashboard.entity.BizRiskAlert;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

public interface BizRiskAlertMapper extends BaseMapper<BizRiskAlert> {
    Map<String, Object> selectRiskSummary();
    List<Map<String, Object>> selectRecentAlerts(@Param("limit") int limit);
    List<Map<String, Object>> selectRiskStatisticsByType();
    List<Map<String, Object>> selectRiskStatisticsByLevel();
    List<Map<String, Object>> selectRiskTrend();
}
