package com.itesm.panoptimize.dto.dashboard;

import java.util.Date;
import java.util.UUID;

public class DashboardDTO {

    private Date startDate;
    private Date endDate;
    private String[] routingProfiles;
    // UUID format
    private String[] queues;

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
}
