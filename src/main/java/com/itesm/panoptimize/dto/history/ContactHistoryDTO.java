package com.itesm.panoptimize.dto.history;

import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

public class ContactHistoryDTO {
    private int contact_id;
    private String agent_name;
    private LocalDate date;
    private LocalTime time;
    private LocalTime duration;
    private String resolution_status;
    private String satisfaction;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public int getContact_id() {
        return contact_id;
    }

    public LocalTime getDuration() {
        return duration;
    }

    public void setDuration(LocalTime duration) {
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

    public String getSatisfaction() {
        return satisfaction;
    }

    public void setSatisfaction(String satisfaction) {
        this.satisfaction = satisfaction;
    }
}
