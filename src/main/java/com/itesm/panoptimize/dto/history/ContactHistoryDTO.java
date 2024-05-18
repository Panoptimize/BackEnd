package com.itesm.panoptimize.dto.history;

import java.time.LocalDateTime;
import java.util.Date;

public class ContactHistoryDTO {
    private int contact_id;
    private String agent_name;
    private int agent_id;
    private LocalDateTime start_time;
    private LocalDateTime end_time;
    private long duration;
    private String resolution_status;
    private int satisfaction;

    public int getContact_id() {
        return contact_id;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getAgent_name() {
        return agent_name;
    }

    public void setAgent_name(String agent_name) {
        this.agent_name = agent_name;
    }

    public void setContact_id(int contact_id) {
        this.contact_id = contact_id;
    }

    public LocalDateTime getStart_time() {
        return start_time;
    }

    public void setStart_time(LocalDateTime start_time) {
        this.start_time = start_time;
    }

    public LocalDateTime getEnd_time() {
        return end_time;
    }

    public void setEnd_time(LocalDateTime end_time) {
        this.end_time = end_time;
    }

    public String getResolution_status() {
        return resolution_status;
    }

    public void setResolution_status(String resolution_status) {
        this.resolution_status = resolution_status;
    }

    public int getAgent_id() {
        return agent_id;
    }

    public void setAgent_id(int agent_id) {
        this.agent_id = agent_id;
    }

    public int getSatisfaction() {
        return satisfaction;
    }

    public void setSatisfaction(int satisfaction) {
        this.satisfaction = satisfaction;
    }
}
