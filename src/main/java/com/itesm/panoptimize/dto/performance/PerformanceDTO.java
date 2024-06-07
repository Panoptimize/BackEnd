package com.itesm.panoptimize.dto.performance;

import jakarta.validation.constraints.NotNull;

import java.util.Date;
import java.util.List;

public class PerformanceDTO {

    @NotNull(message = "Start date is required")
    private Date startDate;

    @NotNull(message = "End date is required")
    private Date endDate;

    private String instanceId;

    @NotNull(message = "Routing profiles IDs are required")
    private List<String> routingProfiles;

    // Getters and Setters
    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public List<String> getRoutingProfileIds() {
        return routingProfiles;
    }

    public void setRoutingProfileIds(List<String> routingProfiles) {
        this.routingProfiles = routingProfiles;
    }

}
