package com.enterprise.dashboard.service.impl;

import com.enterprise.dashboard.dto.*;
import com.enterprise.dashboard.mapper.BizFinancialMonthlyMapper;
import com.enterprise.dashboard.service.FinanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class FinanceServiceImpl implements FinanceService {

    @Autowired
    private BizFinancialMonthlyMapper financialMapper;

    @Override
    public List<MonthlyReportDTO> getMonthlyReport(String year) {
        List<Map<String, Object>> dataList = financialMapper.selectMonthlyReport(year);
        List<MonthlyReportDTO> result = new ArrayList<>();

        for (Map<String, Object> data : dataList) {
            MonthlyReportDTO dto = new MonthlyReportDTO();
            dto.setYearMonth((String) data.get("yearMonth"));

            BigDecimal revenue = toBigDecimal(data.get("revenue"));
            BigDecimal cost = toBigDecimal(data.get("cost"));
            BigDecimal grossProfit = toBigDecimal(data.get("grossProfit"));
            BigDecimal operatingExpense = toBigDecimal(data.get("operatingExpense"));
            BigDecimal netProfit = toBigDecimal(data.get("netProfit"));

            dto.setRevenue(revenue);
            dto.setCost(cost);
            dto.setGrossProfit(grossProfit);
            dto.setOperatingExpense(operatingExpense);
            dto.setNetProfit(netProfit);

            if (revenue.compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal grossMargin = grossProfit.divide(revenue, 4, RoundingMode.HALF_UP)
                        .multiply(new BigDecimal("100"));
                dto.setGrossMargin(grossMargin);

                BigDecimal netMargin = netProfit.divide(revenue, 4, RoundingMode.HALF_UP)
                        .multiply(new BigDecimal("100"));
                dto.setNetMargin(netMargin);
            }

            result.add(dto);
        }

        return result;
    }

    @Override
    public BalanceStructureDTO getBalanceStructure(String yearMonth) {
        Map<String, Object> data = financialMapper.selectLatestBalanceStructure();
        BalanceStructureDTO dto = new BalanceStructureDTO();

        if (data != null) {
            BigDecimal totalAssets = toBigDecimal(data.get("totalAssets"));
            BigDecimal totalLiabilities = toBigDecimal(data.get("totalLiabilities"));
            BigDecimal ownerEquity = toBigDecimal(data.get("ownerEquity"));

            dto.setTotalAssets(totalAssets);
            dto.setTotalLiabilities(totalLiabilities);
            dto.setOwnerEquity(ownerEquity);
            dto.setCashFlow(toBigDecimal(data.get("cashFlow")));
            dto.setAccountsReceivable(toBigDecimal(data.get("accountsReceivable")));
            dto.setAccountsPayable(toBigDecimal(data.get("accountsPayable")));
            dto.setInventoryValue(toBigDecimal(data.get("inventoryValue")));

            if (totalAssets.compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal assetsLiabilityRatio = totalLiabilities.divide(totalAssets, 4, RoundingMode.HALF_UP)
                        .multiply(new BigDecimal("100"));
                dto.setAssetsLiabilityRatio(assetsLiabilityRatio);
            }
        }

        return dto;
    }

    @Override
    public List<CostAnalysisDTO> getCostAnalysis(String year) {
        List<Map<String, Object>> dataList = financialMapper.selectCostAnalysis(year);
        List<CostAnalysisDTO> result = new ArrayList<>();

        for (Map<String, Object> data : dataList) {
            CostAnalysisDTO dto = new CostAnalysisDTO();
            dto.setMonth((String) data.get("month"));
            dto.setProductionCost(toBigDecimal(data.get("productionCost")));
            dto.setOperatingExpense(toBigDecimal(data.get("operatingExpense")));
            dto.setOtherCost(toBigDecimal(data.get("otherCost")));
            result.add(dto);
        }

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
}
