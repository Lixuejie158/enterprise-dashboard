package com.enterprise.dashboard.service;

import com.enterprise.dashboard.dto.*;
import java.util.List;

public interface FinanceService {
    List<MonthlyReportDTO> getMonthlyReport(String year);
    BalanceStructureDTO getBalanceStructure(String yearMonth);
    List<CostAnalysisDTO> getCostAnalysis(String year);
}
