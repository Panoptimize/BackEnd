package com.itesm.panoptimize.dto.agent_performance;

import com.itesm.panoptimize.model.Note;

public class UpdateAgent_PerformanceDTO {
    private Double avgAfterContactWorkTime;
    private Double avgHandleTime;
    private Double avgAbandonTime;
    private Double avgHoldTime;
    private Integer agentId;

    public Double getAvgAfterContactWorkTime() {
        return avgAfterContactWorkTime;
    }

    public void setAvgAfterContactWorkTime(Double avgAfterContactWorkTime) {
        this.avgAfterContactWorkTime = avgAfterContactWorkTime;
    }

    public Double getAvgHandleTime() {
        return avgHandleTime;
    }

    public void setAvgHandleTime(Double avgHandleTime) {
        this.avgHandleTime = avgHandleTime;
    }

    public Double getAvgAbandonTime() {
        return avgAbandonTime;
    }

    public void setAvgAbandonTime(Double avgAbandonTime) {
        this.avgAbandonTime = avgAbandonTime;
    }

    public Double getAvgHoldTime() {
        return avgHoldTime;
    }

    public void setAvgHoldTime(Double avgHoldTime) {
        this.avgHoldTime = avgHoldTime;
    }

    public Integer getAgentId() {
        return agentId;
    }

    public void setAgentId(Integer agentId) {
        this.agentId = agentId;
    }

}
