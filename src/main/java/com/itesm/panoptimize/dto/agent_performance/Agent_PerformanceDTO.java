package com.itesm.panoptimize.dto.agent_performance;

import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public class Agent_PerformanceDTO {
    @NotNull(message = "Id is required")
    private Integer agentPerformanceId;
    private Instant createdAt;
    private Double avgAfterContactWorkTime;
    private Double avgHandleTime;
    private Double avgAbandonTime;
    private Double avgHoldTime;

    public @NotNull(message = "Id is required") Integer getAgentPerformanceId() {
        return agentPerformanceId;
    }

    public void setAgentPerformanceId(@NotNull(message = "Id is required") Integer agentPerformanceId) {
        this.agentPerformanceId = agentPerformanceId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

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
}
