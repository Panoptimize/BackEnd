package com.itesm.panoptimize.dto.performance;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.Date;

public class PerformanceDTO {
    @NotNull(message = "Instance ID is required")
    @Size(min = 36, max = 36, message = "Instance ID must be 36 characters long")
    private String instanceId;
    @NotNull(message = "Start date is required")
    private LocalDate startDate;
    @NotNull(message = "End date is required")
    private LocalDate endDate;
    private String[] routingProfiles;
    // UUID format
    private String[] queues;

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
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
