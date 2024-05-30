package com.itesm.panoptimize.model;


import jakarta.persistence.*;

import java.util.Date;
import java.time.LocalDate;


@Entity
@Table(name = "agent_performance")
public class AgentPerformance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "agent_performance_id")
    private int id;

    @Column(name = "date")
    private LocalDate performanceDate;

    @Column(name = "avg_after_contact_work_time")
    private Double avgAfterContactWorkTime;

    @Column(name = "avg_handle_time")
    private Double avgHandleTime;

    @Column(name = "avg_abandon_time")
    private Double avgAbandonTime;

    @Column(name = "avg_hold_time")
    private Double avgHoldTime;

    @Column(name = "agent_id", length = 20)
    private String agentId;

    // Getters and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getPerformanceDate() {
        return performanceDate;
    }

    public void setPerformanceDate(LocalDate date) {
        this.performanceDate = date;
    }

    public Double getAvgAfterContactWorkTime() {
        return avgAfterContactWorkTime;
    }

    public void setAvgAfterContactWorkTime(Double avgAfterContactWorkTime) {
        this.avgAfterContactWorkTime = avgAfterContactWorkTime;
    }

    public Double getAvgHandleTime() {
        return avgHandleTime;
    }

    public void setAvgHandleTime(Double avgHandleTime) {
        this.avgHandleTime = avgHandleTime;
    }

    public Double getAvgAbandonTime() {
        return avgAbandonTime;
    }

    public void setAvgAbandonTime(Double avgAbandonTime) {
        this.avgAbandonTime = avgAbandonTime;
    }

    public Double getAvgHoldTime() {
        return avgHoldTime;
    }

    public void setAvgHoldTime(Double avgHoldTime) {
        this.avgHoldTime = avgHoldTime;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }
}
