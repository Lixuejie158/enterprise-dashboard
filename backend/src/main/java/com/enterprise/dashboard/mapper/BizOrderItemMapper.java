package com.enterprise.dashboard.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.enterprise.dashboard.entity.BizOrderItem;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

public interface BizOrderItemMapper extends BaseMapper<BizOrderItem> {
    List<Map<String, Object>> selectProductSalesRanking(@Param("limit") int limit);
}
