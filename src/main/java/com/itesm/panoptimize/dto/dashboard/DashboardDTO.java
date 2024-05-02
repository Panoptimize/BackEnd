package com.itesm.panoptimize.dto.dashboard;

import java.util.Date;
import java.util.UUID;

public class DashboardDTO {

    private Date startDate;
    private Date endDate;
    private UUID[] agents;
    // UUID format
    private UUID[] workspaces;

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

    public UUID[] getAgents() {
        return agents;
    }

    public void setAgents(UUID[] agents) {
        this.agents = agents;
    }

    public UUID[] getWorkspaces() {
        return workspaces;
    }

    public void setWorkspaces(UUID[] workspaces) {
        this.workspaces = workspaces;
    }
}
