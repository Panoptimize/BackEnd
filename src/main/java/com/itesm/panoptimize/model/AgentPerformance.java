package com.itesm.panoptimize.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Table(name = "agent_performance", indexes = {
        @Index(name = "agent_created_at_index", columnList = "created_at")
})
public class AgentPerformance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "agent_performance_id")
    private Integer id;

    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    private Instant createdAt;

    @Column(name = "avg_after_contact_work_time", nullable = false)
    private Double avgAfterContactWorkTime;

    @Column(name = "avg_handle_time", nullable = false)
    private Double avgHandleTime;

    @Column(name = "avg_abandon_time", nullable = false)
    private Double avgAbandonTime;

    @Column(name = "avg_hold_time", nullable = false)
    private Double avgHoldTime;

    @ManyToOne
    @JoinColumn(name = "agent_id", nullable = false, foreignKey = @ForeignKey(name = "agent_has_performance"))
    private User agent;

    @OneToOne(mappedBy = "agentPerformance", cascade = CascadeType.ALL)
    private Note note;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Double getAvgAfterCallWorkTime() {
        return avgAfterContactWorkTime;
    }

    public void setAvgAfterCallWorkTime(Double avgAfterContactWorkTime) {
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

    public User getAgent() {
        return agent;
    }

    public void setAgent(User agent) {
        this.agent = agent;
    }

    public Note getNotes() {
        return note;
    }

    public void setNotes(Note note) {
        this.note = note;
    }
}