package com.itesm.panoptimize.dto.history;

import java.time.LocalDateTime;
import java.util.Date;

public class ContactHistoryDTO {
    private int contact_id;
    private String agent_name;
    private Date date;
    private Date time;
    private long duration;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

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

    public String getResolution_status() {
        return resolution_status;
    }

    public void setResolution_status(String resolution_status) {
        this.resolution_status = resolution_status;
    }

    public int getSatisfaction() {
        return satisfaction;
    }

    public void setSatisfaction(int satisfaction) {
        this.satisfaction = satisfaction;
    }
}
