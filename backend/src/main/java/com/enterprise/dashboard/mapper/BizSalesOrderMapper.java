package com.enterprise.dashboard.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.enterprise.dashboard.entity.BizSalesOrder;
import org.apache.ibatis.annotations.Param;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface BizSalesOrderMapper extends BaseMapper<BizSalesOrder> {
    List<Map<String, Object>> selectSalesByRegion();
    List<Map<String, Object>> selectTopCustomers(@Param("limit") int limit);
    BigDecimal selectTotalRevenue();
    Long selectTotalOrderCount();
}
