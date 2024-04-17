package com.itesm.panoptimize.dto.dashboard;

import jakarta.validation.constraints.NotBlank;

/*
POST
/dashboard/data
{
    timeframe: string,
    agent: int []
    workspace: int []
}
*/
public class DashboardDTO {
    @NotBlank
    private String timeframe;
    private int[] agents;
    private int[] workspaces;

    public String getTimeframe() {
        return timeframe;
    }

    public void setTimeframe(String timeframe) {
        this.timeframe = timeframe;
    }

    public int[] getAgents() {
        return agents;
    }

    public void setAgents(int[] agents) {
        this.agents = agents;
    }

    public int[] getWorkspaces() {
        return workspaces;
    }

    public void setWorkspaces(int[] workspaces) {
        this.workspaces = workspaces;
    }
}
