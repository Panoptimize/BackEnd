package com.itesm.panoptimize.dto.history;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

public class ContactDetailsDTO {
    private String full_name;
    private String email;
    private String workspaces;
    private boolean fcr;
    private int after_call_worktime;
    private String satisfaction;
    private LocalTime duration;
    private String status;
    private int answer_speed;
    private LocalDate date;
    private LocalTime time;

    //Insights
    private List<HistoryInsightsDTO> insights;

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWorkspaces() {
        return workspaces;
    }

    public void setWorkspaces(String workspaces) {
        this.workspaces = workspaces;
    }

    public boolean isFcr() {
        return fcr;
    }

    public void setFcr(boolean fcr) {
        this.fcr = fcr;
    }

    public int getAfter_call_worktime() {
        return after_call_worktime;
    }

    public void setAfter_call_worktime(int after_call_worktime) {
        this.after_call_worktime = after_call_worktime;
    }

    public String getSatisfaction() {
        return satisfaction;
    }

    public void setSatisfaction(String satisfaction) {
        this.satisfaction = satisfaction;
    }

    public LocalTime getDuration() {
        return duration;
    }

    public void setDuration(LocalTime duration) {
        this.duration = duration;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getAnswer_speed() {
        return answer_speed;
    }

    public void setAnswer_speed(int answer_speed) {
        this.answer_speed = answer_speed;
    }

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

    public List<HistoryInsightsDTO> getInsights() {
        return insights;
    }

    public void setInsights(List<HistoryInsightsDTO> insights) {
        this.insights = insights;
    }
}
