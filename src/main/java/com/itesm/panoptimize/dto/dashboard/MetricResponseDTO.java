package com.itesm.panoptimize.dto.dashboard;

public class MetricResponseDTO {
    /**
     * Average Hold Time KPI
     */
    private Double avgHoldTime;

    /**
     * Agent Schedule Adherence KPI
     */
    private Double firstContactResolution;

    /**
     * Abandonment Rate KPI
     */
    private Double abandonmentRate;

    /**
     * Service Level KPI
     */
    private Double serviceLevel;

    /**
     * Occupancy KPI
     */
    private Double agentScheduleAdherence;

    /**
     * Average Speed Answer KPI
     */
    private Double avgSpeedOfAnswer;

    public MetricResponseDTO() {
    }

    public MetricResponseDTO(Double avgHoldTime, Double firstContactResolution, Double abandonmentRate, Double serviceLevel, Double agentScheduleAdherence, Double avgSpeedOfAnswer) {
        this.avgHoldTime = avgHoldTime;
        this.firstContactResolution = firstContactResolution;
        this.abandonmentRate = abandonmentRate;
        this.serviceLevel = serviceLevel;
        this.agentScheduleAdherence = agentScheduleAdherence;
        this.avgSpeedOfAnswer = avgSpeedOfAnswer;
    }

    public Double getAvgHoldTime() {
        return avgHoldTime;
    }

    public void setAvgHoldTime(Double avgHoldTime) {
        this.avgHoldTime = avgHoldTime;
    }

    public Double getFirstContactResolution() {
        return firstContactResolution;
    }

    public void setFirstContactResolution(Double firstContactResolution) {
        this.firstContactResolution = firstContactResolution;
    }

    public Double getAbandonmentRate() {
        return abandonmentRate;
    }

    public void setAbandonmentRate(Double abandonmentRate) {
        this.abandonmentRate = abandonmentRate;
    }

    public Double getServiceLevel() {
        return serviceLevel;
    }

    public void setServiceLevel(Double serviceLevel) {
        this.serviceLevel = serviceLevel;
    }

    public Double getAgentScheduleAdherence() {
        return agentScheduleAdherence;
    }

    public void setAgentScheduleAdherence(Double agentScheduleAdherence) {
        this.agentScheduleAdherence = agentScheduleAdherence;
    }

    public Double getAvgSpeedOfAnswer() {
        return avgSpeedOfAnswer;
    }

    public void setAvgSpeedOfAnswer(Double avgSpeedOfAnswer) {
        this.avgSpeedOfAnswer = avgSpeedOfAnswer;
    }
}

