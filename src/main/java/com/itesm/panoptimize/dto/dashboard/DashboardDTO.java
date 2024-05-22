package com.itesm.panoptimize.dto.dashboard;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Date;

public class DashboardDTO {
    @NotNull(message = "Instance ID is required")
    @Size(min = 36, max = 36, message = "Instance ID must be 36 characters long")
    private String instanceId;
    @NotNull(message = "Start date is required")
    private Date startDate;
    @NotNull(message = "End date is required")
    private Date endDate;
    private String[] routingProfiles;
    private String[] agents;

    public DashboardDTO() {
    }

    public DashboardDTO(String instanceId, Date startDate, Date endDate, String[] routingProfiles, String[] agents) {
        this.instanceId = instanceId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.routingProfiles = routingProfiles;
        this.agents = agents;
    }

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

    public String[] getAgents() {
        return agents;
    }

    public void setAgents(String[] agents) {
        this.agents = agents;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }
}
