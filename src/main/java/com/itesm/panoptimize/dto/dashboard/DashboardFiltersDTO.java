package com.itesm.panoptimize.dto.dashboard;

import java.util.List;

public class DashboardFiltersDTO {
    private List<AWSObjDTO> agents;
    private List<AWSObjDTO> workspaces;

    public List<AWSObjDTO> getAgents() {
        return agents;
    }

    public void setAgents(List<AWSObjDTO> agents) {
        this.agents = agents;
    }

    public List<AWSObjDTO> getWorkspaces() {
        return workspaces;
    }

    public void setWorkspaces(List<AWSObjDTO> workspaces) {
        this.workspaces = workspaces;
    }
}
