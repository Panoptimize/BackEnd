package com.itesm.panoptimize.dto.dashboard;

import java.time.LocalDate;
import java.util.List;

public class DashboardFiltersDTO {
    private LocalDate instanceCreationDate;
    private List<AWSObjDTO> workspaces;

    public List<AWSObjDTO> getWorkspaces() {
        return workspaces;
    }

    public void setWorkspaces(List<AWSObjDTO> workspaces) {
        this.workspaces = workspaces;
    }

    public LocalDate getInstanceCreationDate() {
        return instanceCreationDate;
    }

    public void setInstanceCreationDate(LocalDate instanceCreationDate) {
        this.instanceCreationDate = instanceCreationDate;
    }
}
