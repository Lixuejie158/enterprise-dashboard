package com.enterprise.dashboard.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.enterprise.dashboard.entity.BizFinancialMonthly;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

public interface BizFinancialMonthlyMapper extends BaseMapper<BizFinancialMonthly> {
    List<Map<String, Object>> selectRevenueTrend(@Param("year") String year);
    List<Map<String, Object>> selectMonthlyReport(@Param("year") String year);
    Map<String, Object> selectLatestBalanceStructure();
    List<Map<String, Object>> selectCostAnalysis(@Param("year") String year);
}
