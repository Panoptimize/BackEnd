package com.itesm.panoptimize.dto.agent_performance;

import software.amazon.awssdk.annotations.NotNull;

public class CreateAgentPerformanceDTO {
    @NotNull
    private Double avgAfterContactWorkTime;
    @NotNull
    private Double avgHandleTime;
    @NotNull
    private Double avgAbandonTime;
    @NotNull
    private Double avgHoldTime;
    @NotNull
    private Integer id;

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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
