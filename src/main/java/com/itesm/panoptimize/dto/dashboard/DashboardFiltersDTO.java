package com.itesm.panoptimize.dto.dashboard;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class DashboardFiltersDTO {
    private Date instanceCreationDate;
    private List<AWSObjDTO> workspaces;

    public List<AWSObjDTO> getWorkspaces() {
        return workspaces;
    }

    public void setWorkspaces(List<AWSObjDTO> workspaces) {
        this.workspaces = workspaces;
    }

    public Date getInstanceCreationDate() {
        return instanceCreationDate;
    }

    public void setInstanceCreationDate(Date instanceCreationDate) {
        this.instanceCreationDate = instanceCreationDate;
    }
}
