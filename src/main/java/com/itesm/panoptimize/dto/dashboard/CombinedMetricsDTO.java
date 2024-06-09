package com.itesm.panoptimize.dto.dashboard;

import com.itesm.panoptimize.dto.performance.AgentPerformanceDTO;
import java.util.List;

public class CombinedMetricsDTO {
    private MetricResponseDTO metrics;
    private ActivityResponseDTO activities;
    private List<AgentPerformanceDTO> performanceData;
    private int voice;
    private int chat;


    public MetricResponseDTO getMetrics() {
        return metrics;
    }

    public void setMetrics(MetricResponseDTO metrics) {
        this.metrics = metrics;
    }

    public ActivityResponseDTO getActivities() {
        return activities;
    }

    public void setActivities(ActivityResponseDTO activities) {
        this.activities = activities;
    }

    public List<AgentPerformanceDTO> getPerformanceData() {
        return performanceData;
    }

    public void setPerformanceData(List<AgentPerformanceDTO> performanceData) {
        this.performanceData = performanceData;
    }

    public int getVoice() {
        return voice;
    }

    public void setVoice(int voice) {
        this.voice = voice;
    }

    public int getChat() {
        return chat;
    }

    public void setChat(int chat) {
        this.chat = chat;
    }
}
