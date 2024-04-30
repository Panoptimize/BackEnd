package com.itesm.panoptimize.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "contact")
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contact_id")
    private int id;

    @Column(name = "start_time")
    private Date startTime;

    @Column(name = "end_time")
    private Date endTime;

    @Column(name = "resolution_status")
    private String resolutionStatus;

    @Column(name = "first_contact_resolution")
    private boolean firstContactResolution;

    @Column(name = "sentiment_negative")
    private float sentimentNegative;

    @Column(name = "sentiment_positive")
    private float sentimentPositive;

    @Column(name = "agent_id")
    private int agentId;

    @Column(name = "satisfaction")
    private int satisfaction;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getResolutionStatus() {
        return resolutionStatus;
    }

    public void setResolutionStatus(String resolutionStatus) {
        this.resolutionStatus = resolutionStatus;
    }

    public boolean isFirstContactResolution() {
        return firstContactResolution;
    }

    public void setFirstContactResolution(boolean firstContactResolution) {
        this.firstContactResolution = firstContactResolution;
    }

    public float getSentimentNegative() {
        return sentimentNegative;
    }

    public void setSentimentNegative(float sentimentNegative) {
        this.sentimentNegative = sentimentNegative;
    }

    public float getSentimentPositive() {
        return sentimentPositive;
    }

    public void setSentimentPositive(float sentimentPositive) {
        this.sentimentPositive = sentimentPositive;
    }

    public int getAgentId() {
        return agentId;
    }

    public void setAgentId(int agentId) {
        this.agentId = agentId;
    }

    public int getSatisfaction() {
        return satisfaction;
    }

    public void setSatisfaction(int satisfaction) {
        this.satisfaction = satisfaction;
    }
}