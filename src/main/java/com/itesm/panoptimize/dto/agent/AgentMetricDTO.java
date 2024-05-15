package com.itesm.panoptimize.dto.agent;

public class AgentMetricDTO {
    private String agentId;
    private double contactsHandled;
    private double handlingTime; // in hours
    private double performance;

    public AgentMetricDTO(String agentId, double contactsHandled, double handlingTime, double performance) {
        this.agentId = agentId;
        this.contactsHandled = contactsHandled;
        this.handlingTime = handlingTime;
        this.performance = performance;
    }

    // Getters and setters

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public double getContactsHandled() {
        return contactsHandled;
    }

    public void setContactsHandled(double contactsHandled) {
        this.contactsHandled = contactsHandled;
    }

    public double getHandlingTime() {
        return handlingTime;
    }

    public void setHandlingTime(double handlingTime) {
        this.handlingTime = handlingTime;
    }

    public double getPerformance() {
        return performance;
    }

    public void setPerformance(double performance) {
        this.performance = performance;
    }
}
