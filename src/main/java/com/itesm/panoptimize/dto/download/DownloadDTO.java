package com.itesm.panoptimize.dto.download;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.Instant;
import java.util.Date;

public class DownloadDTO {
    @NotNull(message = "Instance ID is required")
    @Size(min = 36, max = 36, message = "Instance ID must be 36 characters long")
    private String instanceId;
    private Date startDate;
    private Date endDate;
    private String[] routingProfiles;
    private String[] queues;
    private String[] agents;

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

    public String[] getAgents() {
        return agents;
    }

    public void setAgents(String[] agents) {
        this.agents = agents;
    }
}
