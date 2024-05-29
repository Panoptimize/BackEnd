package com.itesm.panoptimize.model;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.Date;

@Entity
@Table(name = "agent_performance")
public class AgentPerformance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "agent_performance_id")
    private Integer id;

    @Column(nullable = false)
    private Instant date;

    @Column(name = "avg_after_call_work_time", nullable = false)
    private Double avgAfterCallWork;

    @Column(name = "avg_handle_time", nullable = false)
    private Double avgHandleTime;

    @Column(name = "avg_abandon_time", nullable = false)
    private Double avgAbandonTime;

    @Column(name = "avg_hold_time", nullable = false)
    private Double avgHoldTime;

    @ManyToOne
    @JoinColumn(name = "agent_id", nullable = false)
    private User agent;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public double getAvgAfterCallWork() {
        return avgAfterCallWork;
    }

    public void setAvgAfterCallWork(double avgAfterCallWork) {
        this.avgAfterCallWork = avgAfterCallWork;
    }

    public double getAvgHandleTime() {
        return avgHandleTime;
    }

    public void setAvgHandleTime(double avgHandleTime) {
        this.avgHandleTime = avgHandleTime;
    }

    public double getAvgAbandonTime() {
        return avgAbandonTime;
    }

    public void setAvgAbandonTime(double avgAbandonTime) {
        this.avgAbandonTime = avgAbandonTime;
    }

    public double getAvgHoldTime() {
        return avgHoldTime;
    }

    public void setAvgHoldTime(double avgHoldTime) {
        this.avgHoldTime = avgHoldTime;
    }

    public User getAgent() {
        return agent;
    }

    public void setAgent(User agent) {
        this.agent = agent;
    }
}