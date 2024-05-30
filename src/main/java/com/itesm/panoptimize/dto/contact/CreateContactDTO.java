package com.itesm.panoptimize.dto.contact;

import jakarta.validation.constraints.NotNull;

public class CreateContactDTO {
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

    public @NotNull(message = "AgentId is required") String getAgentId() {
        return agentId;
    }

    public void setAgentId(@NotNull(message = "AgentId is required") String agentId) {
        this.agentId = agentId;
    }
}
