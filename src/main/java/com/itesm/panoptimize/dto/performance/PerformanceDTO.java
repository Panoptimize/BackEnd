package com.itesm.panoptimize.dto.performance;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Date;

/**
 * Data Transfer Object for performance data.
 */
public class PerformanceDTO {
    @NotNull(message = "Instance ID is required")
    @Size(min = 36, max = 36, message = "Instance ID must be 36 characters long")
    private String instanceId;

    @NotNull(message = "Start date is required")
    private Date startDate;

    @NotNull(message = "End date is required")
    private Date endDate;

    private String[] routingProfiles;
    private String[] queues;

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

    public String[] getRoutingProfiles() {
        return routingProfiles;
    }

    public void setRoutingProfiles(String[] routingProfiles) {
        this.routingProfiles = routingProfiles;
    }

    public String[] getQueues() {
        return queues;
    }

    public void setQueues(String[] queues) {
        this.queues = queues;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }
}
