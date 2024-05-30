package com.itesm.panoptimize.dto.contact;

import jakarta.validation.constraints.NotNull;

public class ContactDTO {
    @NotNull(message = "Id is required")
    String id;
    @NotNull(message = "Satisfaction is required")
    private Integer satisfaction;
    @NotNull(message = "AgentId is required")
    private String agentId;

    public @NotNull(message = "Id is required") String getId() {
        return id;
    }

    public void setId(@NotNull(message = "Id is required") String id) {
        this.id = id;
    }

    public @NotNull(message = "Satisfaction is required") Integer getSatisfaction() {
        return satisfaction;
    }

    public void setSatisfaction(@NotNull(message = "Satisfaction is required") Integer satisfaction) {
        this.satisfaction = satisfaction;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }
}
