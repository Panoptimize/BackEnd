package com.itesm.panoptimize.dto.agent;

public class DashboardAgentDTO {
    private String name;
    private AgentStatus status;

    // Constructor
    public DashboardAgentDTO(String name, AgentStatus status) {
        this.name = name;
        this.status = status;
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AgentStatus getStatus() {
        return status;
    }

    public void setStatus(AgentStatus status) {
        this.status = status;
    }

    // Enum for agent statuses
    public enum AgentStatus {
        AVAILABLE,
        IN_CONTACT,
        AFTER_CALL_WORK,
        OFFLINE
    }
}
