package com.enterprise.dashboard.dto;

import lombok.Data;
import java.util.List;

@Data
public class RiskSummaryDTO {
    private Long total;
    private Long high;
    private Long medium;
    private Long low;
    private List<RecentAlertDTO> recentAlerts;

    @Data
    public static class RecentAlertDTO {
        private String title;
        private Integer level;
        private String type;
        private String time;
    }
}
