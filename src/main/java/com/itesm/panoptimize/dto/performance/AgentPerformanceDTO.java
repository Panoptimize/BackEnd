package com.itesm.panoptimize.dto.performance;

import java.util.List;

public class AgentPerformanceDTO {
    private String AgentName;
    private List<Double> Performances;

    public AgentPerformanceDTO(String agentName, List<Double> performances) {
        AgentName = agentName;
        Performances = performances;
    }

    // Getters and Setters
    public String getAgentName() {
        return AgentName;
    }

    public void setAgentName(String agentID) {
        AgentName = agentID;
    }

    public List<Double> getPerformances() {
        return Performances;
    }

    public void setPerformances(List<Double> performances) {
        Performances = performances;
    }
}
