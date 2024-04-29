package com.itesm.panoptimize.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "call")
public class Call {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "call_id")
    private Long callId;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

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

    public Long getCallId() {
        return callId;
    }

    public void setCallId(Long callId) {
        this.callId = callId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
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