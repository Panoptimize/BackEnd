package com.itesm.panoptimize.dto.performance;

import java.util.List;

public class AgentPerformanceDTO {
    private String AgentID;
    private List<Double> Performances;

    public AgentPerformanceDTO(String agentID, List<Double> performances) {
        AgentID = agentID;
        Performances = performances;
    }

    // Getters and Setters
    public String getAgentID() {
        return AgentID;
    }

    public void setAgentID(String agentID) {
        AgentID = agentID;
    }

    public List<Double> getPerformances() {
        return Performances;
    }

    public void setPerformances(List<Double> performances) {
        Performances = performances;
    }
}
