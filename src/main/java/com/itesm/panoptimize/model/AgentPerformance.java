package com.itesm.panoptimize.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "agent_performance")
public class AgentPerformance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "agent_performance_id")
    private int id;

    @Column(nullable = false)
    private Date date;

    @Column(name = "total_contacts_handled", nullable = false)
    private int totalContactsHandled;

    @Column(name = "total_after_call_work", nullable = false)
    private int totalAfterCallWork;

    @Column(name = "adherence_percentage", nullable = false)
    private float adherencePercentage;

    @ManyToOne
    @JoinColumn(name = "agent_id", nullable = false)
    private User agent;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getTotalContactsHandled() {
        return totalContactsHandled;
    }

    public void setTotalContactsHandled(int totalContactsHandled) {
        this.totalContactsHandled = totalContactsHandled;
    }

    public int getTotalAfterCallWork() {
        return totalAfterCallWork;
    }

    public void setTotalAfterCallWork(int totalAfterCallwork) {
        this.totalAfterCallWork = totalAfterCallwork;
    }

    public float getAdherencePercentage() {
        return adherencePercentage;
    }

    public void setAdherencePercentage(float adherencePercentage) {
        this.adherencePercentage = adherencePercentage;
    }

    public User getAgent() {
        return agent;
    }

    public void setAgent(User agent) {
        this.agent = agent;
    }
}