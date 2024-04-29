package com.itesm.panoptimize.dto.dashboard;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public class DashboardDTO {
    @NotBlank
    private String timeframe; // Timeframe format: "2024-04-28 ~ 2024-04-28"
    private int[] agents;
    // UUID format
    private UUID[] workspaceIds;

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

    public UUID[] getWorkspaceIds() {
        return workspaceIds;
    }

    public void setWorkspaceIds(UUID[] workspaceIds) {
        this.workspaceIds = workspaceIds;
    }
}
