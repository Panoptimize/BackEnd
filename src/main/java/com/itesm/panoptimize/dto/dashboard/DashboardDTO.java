package com.itesm.panoptimize.dto.dashboard;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DashboardDTO {
    @NotNull(message = "Start date is required")
    private Date startDate;
    @NotNull(message = "End date is required")
    private Date endDate;
    private List<String> routingProfiles = new ArrayList<>();

    public DashboardDTO() {
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

    public List<String> getRoutingProfiles() {
        return routingProfiles;
    }

    public void setRoutingProfiles(List<String> routingProfiles) {
        this.routingProfiles = routingProfiles;
    }

}
