package com.enterprise.dashboard.dto;

import lombok.Data;
import java.util.List;

@Data
public class RiskStatisticsDTO {
    private List<TypeCount> byType;
    private List<LevelCount> byLevel;
    private List<MonthCount> trend;

    @Data
    public static class TypeCount {
        private String type;
        private Long count;
    }

    @Data
    public static class LevelCount {
        private Integer level;
        private Long count;
    }

    @Data
    public static class MonthCount {
        private String month;
        private Long count;
    }
}
