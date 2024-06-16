package com.itesm.panoptimize.dto.performance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import java.util.Map;

public class MapPerformanceDTO {
    private Map<String, List<Double>> agentPerformances;

    public MapPerformanceDTO() {
        this.agentPerformances = new HashMap<>();
    }

    // Getter and Setter
    public Map<String, List<Double>> getAgentPerformances() {
        return agentPerformances;
    }

    public void setAgentPerformances(Map<String, List<Double>> agentPerformances) {
        this.agentPerformances = agentPerformances;
    }

    public void addAgentPerformance(String agentName, Double performance) {
        if (!this.agentPerformances.containsKey(agentName))
            this.agentPerformances.put(agentName, new ArrayList<>());
        else
            this.agentPerformances.get(agentName).add(performance);
    }
}
